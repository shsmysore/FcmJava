# FcmJava #

## On the Firebase Admin SDK for Java ##

This library was written at a time, when the official Firebase Admin SDK for Java did **not** support the Cloud Messaging API. The most recent release of the Firebase Admin SDK **now also contains the Cloud Messaging**, so the official SDK can be used instead of this library. You can find the official Firebase Admin SDK for Java at: https://firebase.google.com/docs/admin/setup.

## Table of Contents ##

* [Description](#description)
* [Maven Dependencies](#maven-dependencies)
* [Quickstart](#quickstart)
    * [FcmClient](#fcmclient)
    * [FcmClientSettings and API Key](#fcmclientsettings-and-api-key)
    * [Configuring a Proxy](#configuring-a-proxy)
* [FAQ](#quickstart)
    * [How to interpret the FCM Response Messages](#how-to-interpret-the-fcm-response-messages)
* [Android Client](#android-client)
* [Additional Resources](#additional-resources)

## Description ##

[FcmJava] is a library for working with the [Firebase Cloud Messaging (FCM) API].

## Maven Dependencies ##

You can add the following dependencies to your pom.xml to include [FcmJava] in your project.

```xml
<dependency>
  <groupId>io.github.shsmysore.fcmjava</groupId>
  <artifactId>fcmjava-core</artifactId>
  <version>3.3.0</version>
</dependency>

<dependency>
  <groupId>io.github.shsmysore.fcmjava</groupId>
  <artifactId>fcmjava-client</artifactId>
  <version>3.3.0</version>
</dependency>
```

## Quickstart ##

The Quickstart shows you how to work with [FcmJava].

### FcmClient ###

```java
// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.client.FcmClient;
import io.github.shsmysore.fcmjava.constants.Constants;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.model.topics.Topic;
import io.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.Charset;
import java.time.Duration;

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
}
```

### FcmClientSettings and API Key ###

The ``FcmClient`` can be instantiated with ``IFcmClientSettings`` to supply the API Key. By default the ``FcmClient`` uses the 
``PropertiesBasedSettings``, which locate the settings in a default location. If you need to supply the API Key in a different 
way, you can simply instantiate the ``FcmClient`` with a custom ``IFcmClientSettings`` implementation.

#### Using the PropertiesBasedSettings ####

By default the FCM API Key is read from an external ``.properties`` file called ``fcmjava.properties`` to ensure the API Key 
secret does not reside in code or leaks into the public. The default location of the ``fcmjava.properties`` is 
``System.getProperty("user.home") + "/.fcmjava/fcmjava.properties"``.

The file has to contain the FCM API Endpoint and the API Key:

```properties
fcm.api.url=https://fcm.googleapis.com/fcm/send
fcm.api.key=<YOUR_API_KEY_HERE>
```

If the properties are available in the default location you can simply instantiate the ``FcmClient``as seen in the example.

You can use the ``PropertiesBasedSettings`` class to read the Properties and pass them into the ``FcmClient``, if the Properties path differs from the default path:

1. ``PropertiesBasedSettings.createFromDefault()``
    * Uses the default file location of ``System.getProperty("user.home") + "/.fcmjava/fcmjava.properties"`` to read the properties. This is the recommended way of reading your API Key.
2. ``PropertiesBasedSettings.createFromFile(Path path, Charset charset)``
    * Uses a custom file location to read the settings from.
3. ``PropertiesBasedSettings.createFromSystemProperties()``
    * Uses the System Properties to initialize the settings.
4. ``PropertiesBasedSettings.createFromProperties(Properties properties)``
    * Uses the supplied Properties to build the FcmSettings.

#### Implementing the IFcmClientSettings interface ####

It's not neccessary to use the ``PropertiesBasedSettings`` for supplying an API Key to the ``FcmClient``. You can easily implement the ``IFcmClientSettings`` interface 
and pass it into the ``FcmClient``.

The following test shows a simple ``IFcmClientSettings`` implementation, that will be instantiated with the given API Key. Again I strongly suggest to not hardcode the 
Firebase Cloud Messaging API Key in code. This makes it possible to accidentally leak your credentials into public.

```java
// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests.settings;

import io.github.shsmysore.fcmjava.client.FcmClient;
import io.github.shsmysore.fcmjava.constants.Constants;
import io.github.shsmysore.fcmjava.http.client.IFcmClient;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import org.junit.Test;

class FixedFcmClientSettings implements IFcmClientSettings {

    private final String apiKey;

    public FixedFcmClientSettings(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getFcmUrl() {
        return Constants.FCM_URL;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}

public class FcmClientSettingsTest {

    @Test
    public void testFixedClientSettings() {

        // Construct the FCM Client Settings with your API Key:
        IFcmClientSettings clientSettings = new FixedFcmClientSettings("your_api_key_here");

        // Instantiate the FcmClient with the API Key:
        IFcmClient client = new FcmClient(clientSettings);
    }

}
```

### Configuring a Proxy ###

[Apache HttpClient]: http://hc.apache.org/httpcomponents-client-ga/
[HttpClientBuilder]: http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/HttpClientBuilder.html

[FcmJava] uses [Apache HttpClient] for making requests to the Firebase Cloud Messaging server. So in order to configure 
a proxy for the HTTP requests, you can configure the [HttpClientBuilder] used in [FcmJava]. This is done by instantiating 
the ``DefaultHttpClient`` with your configured [HttpClientBuilder].

The following test shows how to build the ``FcmClient`` with a custom ``HttpClient``, which configures a Proxy for the [HttpClientBuilder].

```java
// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests;

import io.github.shsmysore.fcmjava.client.FcmClient;
import io.github.shsmysore.fcmjava.client.http.apache.DefaultHttpClient;
import io.github.shsmysore.fcmjava.http.client.IFcmClient;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.junit.Test;

class FakeFcmClientSettings implements IFcmClientSettings {

    @Override
    public String getFcmUrl() {
        return "";
    }

    @Override
    public String getApiKey() {
        return "";
    }
}

public class HttpBuilderConfigurationTest {


    @Test
    public void testFcmClientWithProxySettings() {

        // Create Settings:
        IFcmClientSettings settings = new FakeFcmClientSettings();

        // Define the Credentials to be used:
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();

        // Set the Credentials (any auth scope used):
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("your_username", "your_password"));

        // Create the Apache HttpClientBuilder:
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                // Set the Proxy Address:
                .setProxy(new HttpHost("your_hostname", 1234))
                // Set the Authentication Strategy:
                .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
                // Set the Credentials Provider we built above:
                .setDefaultCredentialsProvider(basicCredentialsProvider);

        // Create the DefaultHttpClient:
        DefaultHttpClient httpClient = new DefaultHttpClient(settings, httpClientBuilder);

        // Finally build the FcmClient:
        IFcmClient client = new FcmClient(settings, httpClient);
    }
}
```


## Additional Resources ##

* [Send messages from Spring Boot to Ionic 2 over FCM](https://golb.hplar.ch/p/Send-messages-from-Spring-Boot-to-Ionic-2-over-FCM)

[FcmJava]: https://github.com/shsmysore/FcmJava
[Firebase Cloud Messaging (FCM) API]: https://firebase.google.com
