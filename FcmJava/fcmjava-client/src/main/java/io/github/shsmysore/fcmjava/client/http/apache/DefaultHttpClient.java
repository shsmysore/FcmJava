// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.http.apache;

import io.github.shsmysore.fcmjava.client.http.IHttpClient;
import io.github.shsmysore.fcmjava.client.serializer.IJsonSerializer;
import io.github.shsmysore.fcmjava.client.serializer.JsonSerializer;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    private static final HttpClient.Builder defaultHttpBuilder = HttpClient
            .newBuilder()
            .version(HttpClient.Version.HTTP_1_1);

    private final HttpClient client;

    public DefaultHttpClient(IFcmClientSettings settings) {
        this(settings, defaultHttpBuilder);
    }

    public DefaultHttpClient(IFcmClientSettings settings, HttpClient.Builder httpClientBuilder) {
        this(settings, new JsonSerializer(), httpClientBuilder);
    }

    public DefaultHttpClient(IFcmClientSettings settings, IJsonSerializer serializer) {
        this(settings, serializer, defaultHttpBuilder);
    }

    public DefaultHttpClient(IFcmClientSettings settings, IJsonSerializer serializer, HttpClient.Builder httpClientBuilder) {

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

            return client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                    .thenApply(java.net.http.HttpResponse::body)
                    .thenApply(responseBody -> serializer.deserialize(responseBody, responseType));
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
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
