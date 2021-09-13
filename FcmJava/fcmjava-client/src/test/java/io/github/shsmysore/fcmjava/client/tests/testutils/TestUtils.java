// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests.testutils;

import io.github.shsmysore.fcmjava.client.functional.Action0;
import org.junit.Assert;

public class TestUtils {
    public static void assertThrows(Action0 action, Class<?> expectedException) {
        Throwable throwable = null;
        try {
            action.invoke();
        } catch(Throwable t) {
            throwable = t;
        }
        if(throwable == null) {
            Assert.assertEquals(expectedException, null);
        } else {
            Assert.assertEquals(expectedException, throwable.getClass());
        }
    }

    public static void assertDoesNotThrow(Action0 action) {
        assertThrows(action, null);
    }
}
