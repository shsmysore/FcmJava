// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.http.client;

import io.github.shsmysore.fcmjava.requests.data.DataMulticastMessage;
import io.github.shsmysore.fcmjava.requests.data.DataUnicastMessage;
import io.github.shsmysore.fcmjava.requests.groups.AddDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.groups.CreateDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.notification.NotificationMulticastMessage;
import io.github.shsmysore.fcmjava.requests.groups.RemoveDeviceGroupMessage;
import io.github.shsmysore.fcmjava.requests.notification.NotificationUnicastMessage;
import io.github.shsmysore.fcmjava.requests.topic.TopicMulticastMessage;
import io.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import io.github.shsmysore.fcmjava.responses.CreateDeviceGroupMessageResponse;
import io.github.shsmysore.fcmjava.responses.FcmMessageResponse;
import io.github.shsmysore.fcmjava.responses.TopicMessageResponse;

import java.util.concurrent.CompletableFuture;

public interface IFcmClient {

    FcmMessageResponse send(DataMulticastMessage message);

    FcmMessageResponse send(NotificationMulticastMessage notification);

    FcmMessageResponse send(DataUnicastMessage message);

    FcmMessageResponse send(NotificationUnicastMessage notification);

    CreateDeviceGroupMessageResponse send(CreateDeviceGroupMessage message);

    TopicMessageResponse send(TopicUnicastMessage message);

    TopicMessageResponse send(TopicMulticastMessage message);

    void send(RemoveDeviceGroupMessage message);

    void send(AddDeviceGroupMessage message);

    // Async
    CompletableFuture<FcmMessageResponse> sendAsync(DataMulticastMessage message);

    CompletableFuture<FcmMessageResponse> sendAsync(NotificationMulticastMessage notification);

    CompletableFuture<FcmMessageResponse> sendAsync(DataUnicastMessage message);

    CompletableFuture<FcmMessageResponse> sendAsync(NotificationUnicastMessage notification);

    CompletableFuture<CreateDeviceGroupMessageResponse> sendAsync(CreateDeviceGroupMessage message);

    CompletableFuture<TopicMessageResponse> sendAsync(TopicUnicastMessage message);

    CompletableFuture<TopicMessageResponse> sendAsync(TopicMulticastMessage message);

    CompletableFuture<Void> sendAsync(RemoveDeviceGroupMessage message);

    CompletableFuture<Void> sendAsync(AddDeviceGroupMessage message);

}
