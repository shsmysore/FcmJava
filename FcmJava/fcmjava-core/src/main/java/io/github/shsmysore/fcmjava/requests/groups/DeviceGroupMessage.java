// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.requests.groups;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.requests.FcmMulticastMessage;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.model.enums.OperationEnum;

import java.util.List;

public abstract class DeviceGroupMessage extends FcmMulticastMessage<String> {

    private final String notificationKeyName;

    public DeviceGroupMessage(FcmMessageOptions options, List<String> registrationIds, String notificationKeyName) {
        super(options, registrationIds);

        this.notificationKeyName = notificationKeyName;
    }

    @Override
    @JsonProperty("notification_key_name")
    public String getPayload() {
        return notificationKeyName;
    }

    @JsonProperty("operation")
    public abstract OperationEnum getOperation();
}
