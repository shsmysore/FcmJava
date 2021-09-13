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
    public void testFcmClientWithProxySettings() throws Exception {

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
        try(IFcmClient client = new FcmClient(settings, httpClient)) {
            // TODO Work with the Proxy ...
        }
    }
}
