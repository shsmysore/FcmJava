// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.test.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shsmysore.fcmjava.responses.CreateDeviceGroupMessageResponse;
import org.junit.Assert;
import org.junit.Test;

public class CreateDeviceGroupMessageResponseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createDeviceGroupSerializationTest() {
        // https://firebase.google.com/docs/cloud-messaging/android/device-group
        String createDeviceGroupJson = "{\n" +
                "   \"notification_key\": \"APA91bGHXQBB...9QgnYOEURwm0I3lmyqzk2TXQ\"\n" +
                "}";

        CreateDeviceGroupMessageResponse response = null;
        try {
            response = mapper.readValue(createDeviceGroupJson, CreateDeviceGroupMessageResponse.class);
        } catch (Exception e) {
            // Ignore ...
        }

        Assert.assertNotNull(response);

        Assert.assertEquals(response.getNotificationKey(), "APA91bGHXQBB...9QgnYOEURwm0I3lmyqzk2TXQ");
    }

}
