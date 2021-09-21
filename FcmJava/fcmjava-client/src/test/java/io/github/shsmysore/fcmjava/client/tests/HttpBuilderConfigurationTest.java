// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests;

import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
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

        // ToDo: test proxy settings.
    }
}
