// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.http.apache;

import io.github.shsmysore.fcmjava.client.http.IHttpClient;
import io.github.shsmysore.fcmjava.client.serializer.IJsonSerializer;
import io.github.shsmysore.fcmjava.client.serializer.JsonSerializer;
import io.github.shsmysore.fcmjava.exceptions.FcmAuthenticationException;
import io.github.shsmysore.fcmjava.exceptions.FcmBadRequestException;
import io.github.shsmysore.fcmjava.exceptions.FcmGeneralException;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * This DefaultHttpClient is based on the Apache DefaultHttpClient.
 *
 * If you need to configure the Apache DefaultHttpClient (proxy settings, timeouts, ...) you can call the configure(...)
 * method to modify the HttpClientBuilder used for creating Apache DefaultHttpClient instances.
 */
public class DefaultHttpClient implements IHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClient.class);
    private final int MAX_RETRY;
    private static final Random random = new Random();
    private final IFcmClientSettings settings;
    private final IJsonSerializer serializer;

    private static final HttpClient.Builder defaultHttpBuilder = HttpClient
            .newBuilder()
            .version(HttpClient.Version.HTTP_1_1);

    private final HttpClient client;

    public DefaultHttpClient(IFcmClientSettings settings) {
        this(settings, defaultHttpBuilder);
    }

    public DefaultHttpClient(IFcmClientSettings settings, HttpClient.Builder httpClientBuilder) {
        this(settings, new JsonSerializer(), httpClientBuilder, 3);
    }

    public DefaultHttpClient(IFcmClientSettings settings, IJsonSerializer serializer) {
        this(settings, serializer, defaultHttpBuilder, 3);
    }

    public DefaultHttpClient(IFcmClientSettings settings,
                             IJsonSerializer serializer,
                             HttpClient.Builder httpClientBuilder,
                             int maxRetry) {

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
        this.MAX_RETRY = maxRetry;
    }

    private <TRequestMessage> HttpRequest buildPostRequest(TRequestMessage requestMessage) {

        // Get the JSON representation of the given request message:
        String content = serializer.serialize(requestMessage);

        return HttpRequest.newBuilder()
                .uri(URI.create("https://fcm.googleapis.com/fcm/send"))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("key=%s", settings.getApiKey()))
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();
    }

    @Override
    public <TRequestMessage, TResponseMessage> CompletableFuture<TResponseMessage> postAsync(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        try {
            HttpRequest request = buildPostRequest(requestMessage);

            HttpResponse.BodyHandler<String> handler = java.net.http.HttpResponse.BodyHandlers.ofString();
            return retrySend(client, request, handler, 1, null)
                    .thenApply(this::evaluateResponse)
                    .thenApply(java.net.http.HttpResponse::body)
                    .thenApply(responseBody -> serializer.deserialize(responseBody, responseType));
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }

    private <T> CompletableFuture<HttpResponse<T>> retrySend(HttpClient client,
                                                             HttpRequest request,
                                                             HttpResponse.BodyHandler<T> handler,
                                                             int count,
                                                              HttpResponse<T> resp) {
        if ((resp != null && resp.statusCode() < 500) || count > MAX_RETRY) {
            return toResponse(resp);
        } else {
            return client.sendAsync(request, handler)
                    .thenApply(Optional::of)
                    .exceptionally(ex -> {
                        // It is useful to retry on IOExceptions we well, along with server errors.
                        Throwable cause = ex.getCause();
                        if (cause instanceof IOException) {
                            LOGGER.info("IOException - {}. Calling the retrySend again...", cause.getMessage());
                            return Optional.empty();
                        }
                        throw new RuntimeException("FCM call failed", cause);
                    })
                    .thenComposeAsync(res -> {
                        if (res.isEmpty()) {
                            fixedDelay();
                        }
                        HttpResponse<T> currentResponse = res.orElse(null);
                        return retrySend(client, request, handler, count + 1, currentResponse);
                    });
        }
    }

    private <T> CompletableFuture<HttpResponse<T>> toResponse(HttpResponse<T> resp) {
        if (resp != null) {
            return CompletableFuture.completedFuture(resp);
        } else {
            LOGGER.error("Failed on all retries.");
            throw new RuntimeException("All retries failed.");
        }
    }

    private void fixedDelay() {
        try {
            long waitMilliSec = random.nextInt(29000) + 1000;
            Thread.sleep(waitMilliSec);
        } catch (InterruptedException e) {
            LOGGER.warn("Delay interrupted.");
            throw new RuntimeException("Delay interrupted.");
        }
    }

    private <T> HttpResponse<T> evaluateResponse(HttpResponse<T> resp) {

        if (resp.statusCode() == 401 || resp.statusCode() == 403) {
            throw new FcmAuthenticationException("Unauthorized");
        }

        if (resp.statusCode() == 400) {
            throw new FcmBadRequestException("Bad request");
        }

        if (resp.statusCode() >= 500) {
            throw new FcmGeneralException(resp.statusCode(), "Something went wrong.");
        }

        return resp;
    }

    @Override
    public <TRequestMessage, TResponseMessage> TResponseMessage postSync(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        try {
            HttpRequest request = buildPostRequest(requestMessage);

            HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            return serializer.deserialize(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }
}
