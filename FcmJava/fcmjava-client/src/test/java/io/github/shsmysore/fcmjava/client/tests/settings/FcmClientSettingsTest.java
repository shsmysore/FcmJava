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
