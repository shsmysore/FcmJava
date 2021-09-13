// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.fcmjava.client.http.apache;

import de.bytefish.fcmjava.client.http.IHttpClient;
import de.bytefish.fcmjava.client.http.apache.utils.RetryHeaderUtils;
import de.bytefish.fcmjava.client.serializer.IJsonSerializer;
import de.bytefish.fcmjava.client.serializer.JsonSerializer;
import de.bytefish.fcmjava.client.utils.OutParameter;
import de.bytefish.fcmjava.exceptions.*;
import de.bytefish.fcmjava.http.exceptions.HttpCommunicationException;
import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * This DefaultHttpClient is based on the Apache DefaultHttpClient.
 *
 * If you need to configure the Apache DefaultHttpClient (proxy settings, timeouts, ...) you can call the configure(...)
 * method to modify the HttpClientBuilder used for creating Apache DefaultHttpClient instances.
 */
public class DefaultHttpClient implements IHttpClient {

    private final IFcmClientSettings settings;
    private final IJsonSerializer serializer;
    private final CloseableHttpClient client;

    private static final HttpClient shreeClient = HttpClient.newHttpClient();

    public DefaultHttpClient(IFcmClientSettings settings) {
        this(settings, HttpClientBuilder.create());
    }

    public DefaultHttpClient(IFcmClientSettings settings, HttpClientBuilder httpClientBuilder) {
        this(settings, new JsonSerializer(), httpClientBuilder);
    }

    public DefaultHttpClient(IFcmClientSettings settings, IJsonSerializer serializer) {
        this(settings, serializer, HttpClientBuilder.create());
    }

    public DefaultHttpClient(IFcmClientSettings settings, IJsonSerializer serializer, HttpClientBuilder httpClientBuilder) {

        if (settings == null) {
            throw new IllegalArgumentException("settings");
        }

        if (httpClientBuilder == null) {
            throw new IllegalArgumentException("httpClientBuilder");
        }

        if(serializer == null) {
            throw new IllegalArgumentException("serializer");
        }

        this.settings = settings;
        this.serializer = serializer;
        this.client = httpClientBuilder.build();
    }


    private <TRequestMessage> void internalPost(TRequestMessage requestMessage) throws IOException {

        // Execute the Request:
        try (CloseableHttpResponse response = client.execute(buildPostRequest(requestMessage))) {

            // Evaluate the Response:
            evaluateResponse(response);

            // Get the HttpEntity:
            HttpEntity entity = response.getEntity();

            // Let's be a good citizen and consume the HttpEntity:
            if (entity != null) {

                // Make Sure it is fully consumed:
                EntityUtils.consume(entity);
            }
        }

    }

    private <TRequestMessage, TResponseMessage> TResponseMessage internalPost(TRequestMessage requestMessage, Class<TResponseMessage> responseType) throws IOException {

        // Execute the Request:
        try (CloseableHttpResponse response = client.execute(buildPostRequest(requestMessage))) {

            // Evaluate the Response:
            evaluateResponse(response);

            // Get the HttpEntity of the Response:
            HttpEntity entity = response.getEntity();

            // If we don't have a HttpEntity, we won't be able to convert it:
            if (entity == null) {
                // Simply return null (no response) in this case:
                return null;
            }

            // Get the JSON Body:
            String responseBody = EntityUtils.toString(entity);

            // Make Sure it is fully consumed:
            EntityUtils.consume(entity);

            // And finally return the Response Message:
            return serializer.deserialize(responseBody, responseType);
        }
    }

    private <TRequestMessage> HttpUriRequest buildPostRequest(TRequestMessage requestMessage) {

        // Get the JSON representation of the given request message:
        String content = serializer.serialize(requestMessage);

        return RequestBuilder.post(settings.getFcmUrl())
                .addHeader(HttpHeaders.AUTHORIZATION, String.format("key=%s", settings.getApiKey()))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setEntity(new StringEntity(content, StandardCharsets.UTF_8))
                .build();
    }

    private <TRequestMessage> HttpRequest shreeBuildPostRequest(TRequestMessage requestMessage) {

        // Get the JSON representation of the given request message:
        String content = serializer.serialize(requestMessage);

        return HttpRequest.newBuilder()
                .uri(URI.create("https://fcm.googleapis.com/fcm/send"))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("key=%s", settings.getApiKey()))
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();
    }

    private void evaluateResponse(HttpResponse httpResponse) {

        // Early exit, if there is no HTTP Response:
        if (httpResponse == null) {
            return;
        }

        // Early exit, if we can't determine the Status:
        if (httpResponse.getStatusLine() == null) {
            return;
        }

        // Get the HTTP Status Code:
        int httpStatusCode = httpResponse.getStatusLine().getStatusCode();

        // Is it OK? So we can exit here:
        if (httpStatusCode == HttpStatus.SC_OK) {
            return;
        }

        // The Error Reason:
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();

        // If it is a Bad Request, we could not retry it:
        if (httpStatusCode == HttpStatus.SC_BAD_REQUEST) {
            throw new FcmBadRequestException(reasonPhrase);
        }

        // If we are unauthorized, we could not retry it:
        if (httpStatusCode == HttpStatus.SC_UNAUTHORIZED) {
            throw new FcmAuthenticationException(reasonPhrase);
        }

        // Any Status Code between 500 and 600 could be retried:
        if (httpStatusCode >= 500 && httpStatusCode < 600) {

            // Holds the Duration, which has been sent by the Server:
            OutParameter<Duration> result = new OutParameter<>();

            // Try to determine the next interval we can send at:
            if (RetryHeaderUtils.tryDetermineRetryDelay(httpResponse, result)) {
                throw new FcmRetryAfterException(httpStatusCode, reasonPhrase, result.get());
            }
        }

        throw new FcmGeneralException(httpStatusCode, reasonPhrase);
    }

    @Override
    public <TRequestMessage> void post(TRequestMessage requestMessage) {
        try {
            internalPost(requestMessage);
        } catch (IOException e) {
            throw new HttpCommunicationException("Error making POST Request", e);
        }
    }

    @Override
    public <TRequestMessage, TResponseMessage> TResponseMessage post(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        try {
            return internalPost(requestMessage, responseType);
        } catch (IOException e) {
            throw new HttpCommunicationException("Error making POST Request", e);
        }
    }

    @Override
    public <TRequestMessage> CompletableFuture<String> postAsync(TRequestMessage requestMessage) {
        try {
            HttpRequest request = shreeBuildPostRequest(requestMessage);

            return shreeClient.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                    .thenApply(java.net.http.HttpResponse::body);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
