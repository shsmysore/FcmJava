// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.client.FcmClient;
import io.github.shsmysore.fcmjava.client.settings.PropertiesBasedSettings;
import io.github.shsmysore.fcmjava.constants.Constants;
import io.github.shsmysore.fcmjava.exceptions.FcmAuthenticationException;
import io.github.shsmysore.fcmjava.exceptions.FcmBadRequestException;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.model.topics.Topic;
import io.github.shsmysore.fcmjava.requests.data.DataMulticastMessage;
import io.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import io.github.shsmysore.fcmjava.responses.FcmMessageResponse;
import io.github.shsmysore.fcmjava.responses.TopicMessageResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

public class FcmClientIntegrationTest {

    private class PersonData {

        private final String firstName;
        private final String lastName;

        public PersonData(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @JsonProperty("firstName")
        public String getFirstName() {
            return firstName;
        }

        @JsonProperty("lastName")
        public String getLastName() {
            return lastName;
        }
    }

    @Test
    @Ignore("This is an Integration Test using system properties to contact the FCM Server")
    public void SendTopicMessageTest() throws Exception {

        // Create the Client using system-properties-based settings:
        try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault())) {

            // Message Options:
            FcmMessageOptions options = FcmMessageOptions.builder()
                    .setTimeToLive(Duration.ofHours(1))
                    .build();

            // Send a Message:
            TopicMessageResponse response = client.send(new TopicUnicastMessage(options, new Topic("news"), new PersonData("Philipp", "Wagner")));

            // Assert Results:
            Assert.assertNotNull(response);

            // Make sure there are no errors:
            Assert.assertNotNull(response.getMessageId());
            Assert.assertNull(response.getErrorCode());
        }
    }

    @Test
    @Ignore("This is an Integration Test using system properties to contact the FCM Server")
    public void SendDataMulticastMessageTest() throws Exception {

        // Create the Client using system-properties-based settings:
        try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault())) {

            // Message Options:
            FcmMessageOptions options = FcmMessageOptions.builder()
                    .setTimeToLive(Duration.ofHours(1))
                    .build();

            ArrayList<String> registrationIds = new ArrayList<>();
            registrationIds.add("invalid_key");

            // Send a Message:
            FcmMessageResponse msgResponse = client.send(new DataMulticastMessage(options, registrationIds, new PersonData("Philipp", "Wagner")));

            Assert.assertNotNull(msgResponse);

        }
    }

    @Test
    @Ignore("This is an Integration Test provoking a Bad Request Exception")
    public void SendDataBadRequestTest() throws Exception {

        // Create the Client using system-properties-based settings:
        try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault())) {

            // Message Options:
            FcmMessageOptions options = FcmMessageOptions.builder()
                    .setTimeToLive(Duration.ofDays(356))
                    .build();

            ArrayList<String> registrationIds = new ArrayList<>();
            registrationIds.add("invalid_key");

            // Send a Message:
            boolean caughtFcmBadRequestException = false;
            try {
                FcmMessageResponse msgResponse = client.send(new DataMulticastMessage(options, registrationIds, ""));
            } catch (FcmBadRequestException e) {
                caughtFcmBadRequestException = true;
                Assert.assertEquals("Bad Request", e.getMessage());
            }

            Assert.assertEquals(true, caughtFcmBadRequestException);
        }
    }

    @Test
    @Ignore("This is an Integration Test provoking an Authentication Exception")
    public void SendDataMulticastMessageWithExceptionTest() throws Exception {

        // Create a Client with an Invalid API Key:
        try (FcmClient client = new FcmClient(new IFcmClientSettings() {
            @Override
            public String getFcmUrl() {
                return Constants.FCM_URL;
            }

            @Override
            public String getApiKey() {
                return "aa";
            }
        })) {

            // Message Options:
            FcmMessageOptions options = FcmMessageOptions.builder()
                    .setTimeToLive(Duration.ofHours(1))
                    .build();

            ArrayList<String> registrationIds = new ArrayList<>();

            registrationIds.add("invalid_key");

            // We want to catch an FcmAuthenticationException:
            boolean fcmAuthenticationExceptionThrown = false;

            // Send the Data and catch the FcmAuthenticationException:
            try {
                client.send(new DataMulticastMessage(options, registrationIds, new PersonData("Philipp", "Wagner")));
            } catch (FcmAuthenticationException e) {
                fcmAuthenticationExceptionThrown = true;
            }

            // The Authentication Exception was caught:
            Assert.assertEquals(true, fcmAuthenticationExceptionThrown);
        }
    }
}
