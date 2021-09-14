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
        return post(message, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(NotificationMulticastMessage notification) {
        return post(notification, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(DataUnicastMessage message) {
        return post(message, FcmMessageResponse.class);
    }

    @Override
    public FcmMessageResponse send(NotificationUnicastMessage notification) {
        return post(notification, FcmMessageResponse.class);
    }

    @Override
    public CreateDeviceGroupMessageResponse send(CreateDeviceGroupMessage message) {
        return post(message, CreateDeviceGroupMessageResponse.class);
    }

    @Override
    public TopicMessageResponse send(TopicUnicastMessage message) {
        return post(message, TopicMessageResponse.class);
    }

    @Override
    public TopicMessageResponse send(TopicMulticastMessage message) {
        return post(message, TopicMessageResponse.class);
    }

    @Override
    public void send(RemoveDeviceGroupMessage message) {
        post(message);
    }

    @Override
    public void send(AddDeviceGroupMessage message) {
        post(message);
    }

    protected <TRequestMessage, TResponseMessage> TResponseMessage post(TRequestMessage requestMessage, Class<TResponseMessage> responseType) {
        return httpClient.post(requestMessage, responseType);
    }

    public <TRequestMessage> CompletableFuture<String> postAsync(TRequestMessage requestMessage) {
        return httpClient.postAsync(requestMessage);
    }

    protected <TRequestMessage> void post(TRequestMessage requestMessage) {
        httpClient.post(requestMessage);
    }

    @Override
    public void close() throws Exception {
        httpClient.close();
    }
}
