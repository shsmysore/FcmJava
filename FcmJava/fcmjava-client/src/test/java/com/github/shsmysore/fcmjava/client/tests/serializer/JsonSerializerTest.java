// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.tests.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.shsmysore.fcmjava.client.serializer.IJsonSerializer;
import com.github.shsmysore.fcmjava.client.serializer.JsonSerializer;
import org.junit.Assert;
import org.junit.Test;

public class JsonSerializerTest {

    public class GermanUmlautEntity {

        private final String content;

        @JsonCreator
        public GermanUmlautEntity(@JsonProperty("content") String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    @Test
    public void umlautsSerializeTest() {

        final IJsonSerializer jsonSerializer = new JsonSerializer();

        JsonSerializerTest.GermanUmlautEntity entity = new JsonSerializerTest.GermanUmlautEntity("Bitteschön. Dankeschön.");

        Assert.assertEquals("{\"content\":\"Bitteschön. Dankeschön.\"}", jsonSerializer.serialize(entity));
    }

}
