// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.settings;

import io.github.shsmysore.fcmjava.client.utils.PropertiesUtils;
import io.github.shsmysore.fcmjava.constants.Constants;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Initializes Client Settings from Properties.
 */
public class PropertiesBasedSettings implements IFcmClientSettings {

    private final String fcmUrl;
    private final String fcmApiKey;

    private PropertiesBasedSettings(Properties properties) {
        fcmUrl = properties.getProperty("fcm.api.url", Constants.FCM_URL);
        fcmApiKey = properties.getProperty("fcm.api.key");
    }

    @Override
    public String getFcmUrl() {
        return fcmUrl;
    }

    @Override
    public String getApiKey() {
        return fcmApiKey;
    }

    /**
     * Creates the Settings from the default location.
     *
     * @return Initialized Client Settings
     */
    public static PropertiesBasedSettings createFromDefault() {

        // Get the default location for the API Key:
        String defaultPropertiesLocationString = System.getProperty("user.home") + "/.fcmjava/fcmjava.properties";

        // Get the Default Properties as Path:
        Path defaultPropertiesLocationPath = FileSystems.getDefault().getPath(defaultPropertiesLocationString);

        return createFromFile(defaultPropertiesLocationPath, StandardCharsets.UTF_8);
    }

    /**
     * Reads the Properties from a given location.
     *
     * @param path Path of the Properties file
     * @param charset Charset of the Properties firle
     * @return Initialized Client Settings
     */
    public static PropertiesBasedSettings createFromFile(Path path, Charset charset) {
        Properties properties = PropertiesUtils.loadProperties(path, charset);

        return new PropertiesBasedSettings(properties);
    }

    /**
     * Reads the Properties from the System Properties.
     *
     * @return Initialized Client Settings
     */
    public static PropertiesBasedSettings createFromSystemProperties() {
        Properties properties = System.getProperties();

        return new PropertiesBasedSettings(properties);
    }

    /**
     * Reads the properties from a Properties object.
     *
     * @param properties Properties instance
     * @return Initialized Client Settings
     */
    public static PropertiesBasedSettings createFromProperties(Properties properties) {
        return new PropertiesBasedSettings(properties);
    }

}
