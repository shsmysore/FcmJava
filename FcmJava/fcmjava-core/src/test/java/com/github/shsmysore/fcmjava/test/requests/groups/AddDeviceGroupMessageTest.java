// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.test.requests.groups;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shsmysore.fcmjava.requests.groups.AddDeviceGroupMessage;
import com.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddDeviceGroupMessageTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void addDeviceGroupSerializationTest() throws Exception {

        // General Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setCollapseKey("collapse")
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Registration IDs to send to:
        List<String> registrationIds = Arrays.asList("123", "456");

        // The Notification Key:
        String notificationKeyName = "notificationKeyName";
        String notificationKey = "notificationKey";

        // Create the AddDeviceGroupMessage:
        AddDeviceGroupMessage message = new AddDeviceGroupMessage(options, registrationIds, notificationKeyName, notificationKey);

        // Serialize it as a JSON String:
        String jsonResult = mapper.writeValueAsString(message);

        // Read as Map:
        Map<String, Object> map = new ObjectMapper()
                .readerFor(Map.class)
                .readValue(jsonResult);

        // There should only be 6 values:
        Assert.assertEquals(6, map.size());

        // Check Registration IDs:
        ArrayList<String> resultRegistrationIds = (ArrayList<String>) map.get("registration_ids");

        Assert.assertEquals(2, resultRegistrationIds.size());
        Assert.assertEquals("123", resultRegistrationIds.get(0));
        Assert.assertEquals("456", resultRegistrationIds.get(1));

        // Check NotificationKey:
        Assert.assertEquals(notificationKey, map.get("notification_key"));

        // Check NotificationKeyName:
        Assert.assertEquals(notificationKeyName, map.get("notification_key_name"));

        // Check Operation:
        Assert.assertEquals("add", map.get("operation"));

        // Check TTL:
        Assert.assertEquals(3600, (int) map.get("time_to_live"));

        // Check CollapseKey:
        Assert.assertEquals("collapse", map.get("collapse_key"));

    }

}
