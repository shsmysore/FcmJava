// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.requests.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.shsmysore.fcmjava.requests.notification.NotificationPayload;
import com.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import com.github.shsmysore.fcmjava.requests.FcmMulticastMessage;

import java.util.List;

public class DataMulticastMessage extends FcmMulticastMessage<Object> {

    private final Object data;
    private final NotificationPayload notification;

    public DataMulticastMessage(FcmMessageOptions options, List<String> registratiodIds, Object data) {
        this(options, registratiodIds, data, null);
    }

    public DataMulticastMessage(FcmMessageOptions options, List<String> registratiodIds, Object data, NotificationPayload notification) {
        super(options, registratiodIds);

        this.data = data;
        this.notification = notification;
    }

    @Override
    @JsonProperty("data")
    public Object getPayload() {
        return data;
    }

    @JsonProperty("notification")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public NotificationPayload getNotificationPayload() {
        return this.notification;
    }

}
