// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.requests.groups;

import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.model.enums.OperationEnum;

import java.util.List;

public class CreateDeviceGroupMessage extends DeviceGroupMessage {

    public CreateDeviceGroupMessage(FcmMessageOptions options, List<String> registrationIds, String notificationKeyName) {
        super(options, registrationIds, notificationKeyName);
    }

    @Override
    public OperationEnum getOperation() {
        return OperationEnum.Create;
    }

}
