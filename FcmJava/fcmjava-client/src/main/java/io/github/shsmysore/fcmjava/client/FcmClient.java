// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client;

import io.github.shsmysore.fcmjava.client.http.IHttpClient;
import io.github.shsmysore.fcmjava.client.http.apache.DefaultHttpClient;
import io.github.shsmysore.fcmjava.client.settings.PropertiesBasedSettings;
import io.github.shsmysore.fcmjava.http.client.IFcmClient;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import io.github.shsmysore.fcmjava.requests.data.DataMulticastMessage;
import io.github.shsmysore.fcmjava.requests.data.DataUnicastMessage;
import io.github.shsmysore.fcmjava.requests.groups.AddDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.groups.CreateDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.groups.RemoveDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.notification.NotificationMulticastMessage;
import io.github.shsmysore.fcmjava.requests.notification.NotificationUnicastMessage;
import io.github.shsmysore.fcmjava.requests.topic.TopicMulticastMessage;
import io.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import io.github.shsmysore.fcmjava.responses.CreateDeviceGroupMessageResponse;
import io.github.shsmysore.fcmjava.responses.FcmMessageResponse;
import io.github.shsmysore.fcmjava.responses.TopicMessageResponse;

import java.util.concurrent.CompletableFuture;

public class FcmClient implements IFcmClient {

    private final IFcmClientSettings settings;
    private final IHttpClient httpClient;

    public FcmClient() {
        this(PropertiesBasedSettings.createFromDefault());
    }

    public FcmClient(IFcmClientSettings settings) {
        this(settings, new DefaultHttpClient(settings));
    }

    public FcmClient(IFcmClientSettings settings, IHttpClient httpClient) {

        if (settings == null) {
            throw new IllegalArgumentException("settings");
        }

        if (httpClient == null) {
            throw new IllegalArgumentException("httpClient");
        }

        this.settings = settings;
        this.httpClient = httpClient;
    }

    @Override
    public FcmMessageResponse send(DataMulticastMessage message) {
        return postSync(message, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(NotificationMulticastMessage notification) {
        return postSync(notification, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(DataUnicastMessage message) {
        return postSync(message, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(NotificationUnicastMessage notification) {
        return postSync(notification, FcmMessageResponse.class);
    }

    @Override
    public CreateDeviceGroupMessageResponse send(CreateDeviceGroupMessage message) {
        return postSync(message, CreateDeviceGroupMessageResponse.class);
    }

    @Override
    public TopicMessageResponse send(TopicUnicastMessage message) {
        return postSync(message, TopicMessageResponse.class);
    }

    @Override
    public TopicMessageResponse send(TopicMulticastMessage message) {
        return postSync(message, TopicMessageResponse.class);
    }

    @Override
    public void send(RemoveDeviceGroupMessage message) {
        postSync(message, Void.class);
    }

    @Override
    public void send(AddDeviceGroupMessage message) {
        postSync(message, Void.class);
    }

    // Async
    @Override
    public CompletableFuture<FcmMessageResponse> sendAsync(DataMulticastMessage message) {
        return postAsync(message, FcmMessageResponse.class);
    }

    @Override
    public CompletableFuture<FcmMessageResponse> sendAsync(NotificationMulticastMessage notification) {
        return postAsync(notification, FcmMessageResponse.class);
    }

    @Override
    public CompletableFuture<FcmMessageResponse> sendAsync(DataUnicastMessage message) {
        return postAsync(message, FcmMessageResponse.class);
    }

    @Override
    public CompletableFuture<FcmMessageResponse> sendAsync(NotificationUnicastMessage notification) {
        return postAsync(notification, FcmMessageResponse.class);
    }

    @Override
    public CompletableFuture<CreateDeviceGroupMessageResponse> sendAsync(CreateDeviceGroupMessage message) {
        return postAsync(message, CreateDeviceGroupMessageResponse.class);
    }

    @Override
    public CompletableFuture<TopicMessageResponse> sendAsync(TopicUnicastMessage message) {
        return postAsync(message, TopicMessageResponse.class);
    }

    @Override
    public CompletableFuture<TopicMessageResponse> sendAsync(TopicMulticastMessage message) {
        return postAsync(message, TopicMessageResponse.class);
    }

    @Override
    public CompletableFuture<Void> sendAsync(RemoveDeviceGroupMessage message) {
        return postAsync(message, Void.class);
    }

    @Override
    public CompletableFuture<Void> sendAsync(AddDeviceGroupMessage message) {
        return postAsync(message, Void.class);
    }

    protected <TRequestMessage, TResponseMessage> TResponseMessage postSync(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        return httpClient.postSync(requestMessage, responseType);
    }

    public <TRequestMessage, TResponseMessage> CompletableFuture<TResponseMessage> postAsync(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        return httpClient.postAsync(requestMessage, responseType);
    }
}
