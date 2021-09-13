// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.http.client;

import com.github.shsmysore.fcmjava.requests.data.DataMulticastMessage;
import com.github.shsmysore.fcmjava.requests.data.DataUnicastMessage;
import com.github.shsmysore.fcmjava.requests.groups.AddDeviceGroupMessage;
import com.github.shsmysore.fcmjava.requests.groups.CreateDeviceGroupMessage;
import com.github.shsmysore.fcmjava.requests.notification.NotificationMulticastMessage;
import com.github.shsmysore.fcmjava.requests.groups.RemoveDeviceGroupMessage;
import com.github.shsmysore.fcmjava.requests.notification.NotificationUnicastMessage;
import com.github.shsmysore.fcmjava.requests.topic.TopicMulticastMessage;
import com.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import com.github.shsmysore.fcmjava.responses.CreateDeviceGroupMessageResponse;
import com.github.shsmysore.fcmjava.responses.FcmMessageResponse;
import com.github.shsmysore.fcmjava.responses.TopicMessageResponse;

public interface IFcmClient extends AutoCloseable {

    FcmMessageResponse send(DataMulticastMessage message);

    FcmMessageResponse send(NotificationMulticastMessage notification);

    FcmMessageResponse send(DataUnicastMessage message);

    FcmMessageResponse send(NotificationUnicastMessage notification);

    CreateDeviceGroupMessageResponse send(CreateDeviceGroupMessage message);

    TopicMessageResponse send(TopicUnicastMessage message);

    TopicMessageResponse send(TopicMulticastMessage message);

    void send(RemoveDeviceGroupMessage message);

    void send(AddDeviceGroupMessage message);

}
