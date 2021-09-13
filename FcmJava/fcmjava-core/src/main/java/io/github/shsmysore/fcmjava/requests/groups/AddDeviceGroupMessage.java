// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.requests.groups;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.model.enums.OperationEnum;

import java.util.List;

public class AddDeviceGroupMessage extends DeviceGroupMessage {

    private final String notificationKey;

    public AddDeviceGroupMessage(FcmMessageOptions options, List<String> registrationIds, String notificationKeyName, String notificationKey) {
        super(options, registrationIds, notificationKeyName);

        this.notificationKey = notificationKey;
    }

    @Override
    public OperationEnum getOperation() {
        return OperationEnum.Add;
    }

    @JsonProperty("notification_key")
    public String getNotificationKey() {
        return this.notificationKey;
    }

}
