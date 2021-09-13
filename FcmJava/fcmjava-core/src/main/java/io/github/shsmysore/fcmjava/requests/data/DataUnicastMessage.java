// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.requests.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.requests.notification.NotificationPayload;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.requests.FcmUnicastMessage;

public class DataUnicastMessage extends FcmUnicastMessage<Object> {

    private final Object data;
    private final NotificationPayload notification;

    public DataUnicastMessage(FcmMessageOptions options, String to, Object data) {
        this(options, to, data, null);
    }

    public DataUnicastMessage(FcmMessageOptions options, String to, Object data, NotificationPayload notification) {
        super(options, to);

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
