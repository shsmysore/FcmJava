// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.test.requests.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shsmysore.fcmjava.model.topics.Topic;
import com.github.shsmysore.fcmjava.requests.notification.NotificationPayload;
import com.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import com.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.Map;

class SampleData {

    private final int value;

    public SampleData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class TopicUnicastMessageTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void serializeTopicWithoutDataTest() throws Exception {

        // General Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setCollapseKey("collapse")
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Build the Notification:
        NotificationPayload notificationPayload = NotificationPayload.builder()
                .setBody("ABC")
                .build();

        // Create the AddDeviceGroupMessage:
        TopicUnicastMessage message = new TopicUnicastMessage(options, new Topic("test"), notificationPayload);

        // Serialize it as a JSON String:
        String jsonResult = mapper.writeValueAsString(message);

        // Read as Map:
        Map<String, Object> map = new ObjectMapper()
                .readerFor(Map.class)
                .readValue(jsonResult);

        Assert.assertFalse(map.containsKey("data"));
        Assert.assertTrue(map.containsKey("notification"));
    }

    @Test
    public void serializeTopicWithoutNotificationPayloadTest() throws Exception {

        // General Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setCollapseKey("collapse")
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Sample Data:
        SampleData sampleData = new SampleData(1);

        // Create the AddDeviceGroupMessage:
        TopicUnicastMessage message = new TopicUnicastMessage(options, new Topic("test"), sampleData);

        // Serialize it as a JSON String:
        String jsonResult = mapper.writeValueAsString(message);

        // Read as Map:
        Map<String, Object> map = new ObjectMapper()
                .readerFor(Map.class)
                .readValue(jsonResult);

        Assert.assertTrue(map.containsKey("data"));
        Assert.assertFalse(map.containsKey("notification"));
    }

    @Test
    public void serializeTopicWithNotificationPayloadAndDataTest() throws Exception {

        // General Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setCollapseKey("collapse")
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Build the Notification:
        NotificationPayload notificationPayload = NotificationPayload.builder()
                .setBody("ABC")
                .build();

        // Sample Data:
        SampleData sampleData = new SampleData(1);

        // Create the AddDeviceGroupMessage:
        TopicUnicastMessage message = new TopicUnicastMessage(options, new Topic("test"), sampleData, notificationPayload);

        // Serialize it as a JSON String:
        String jsonResult = mapper.writeValueAsString(message);

        // Read as Map:
        Map<String, Object> map = new ObjectMapper()
                .readerFor(Map.class)
                .readValue(jsonResult);

        Assert.assertTrue(map.containsKey("data"));
        Assert.assertTrue(map.containsKey("notification"));
    }
}
