// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesUtils {

    /**
     * Loads a Poperties file from a given Path using a given Charset.
     *
     * @param path The File to read the Properties from.
     * @param charset The Charset used for reading the Properties file.
     * @return The Properties of the given file.
     */
    public static Properties loadProperties(Path path, Charset charset) {
        try {
            // Get a Reader for the given File:
            BufferedReader bufferedReader = Files.newBufferedReader(path, charset);
            // Load the Properties:
            return loadProperties(bufferedReader);
        } catch (Exception e) {
            throw new RuntimeException("Could not read Properties from Path " + path, e);
        }
    }

    /**
     *  Loads Properties from a given Reader.
     *
     * @param reader The reader used for parsing the Properties.
     * @return The Properties of the given Reader.
     */
    public static Properties loadProperties(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch(Exception e) {
            throw new RuntimeException("Could not parse Properties", e);
        }
        return properties;
    }

}
