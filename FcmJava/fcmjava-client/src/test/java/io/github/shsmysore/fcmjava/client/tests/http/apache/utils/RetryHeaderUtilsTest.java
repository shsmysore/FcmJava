// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.tests.http.apache.utils;

import io.github.shsmysore.fcmjava.client.http.apache.utils.RetryHeaderUtils;
import io.github.shsmysore.fcmjava.client.utils.DateUtils;
import io.github.shsmysore.fcmjava.client.utils.OutParameter;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetryHeaderUtilsTest {

    @Mock
    private HttpResponse httpResponseMock;

    @Mock
    private Header headerMock;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void noHeaderFoundTest() {
        when(httpResponseMock.getFirstHeader("Retry-After"))
                .thenReturn(null);

        // Holds the Result:
        OutParameter<Duration> result = new OutParameter<>();

        // Try to get the Result:
        boolean success = RetryHeaderUtils.tryDetermineRetryDelay(httpResponseMock, result);

        // Assertions:
        Assert.assertEquals(false, success);
    }

    @Test
    public void headerFoundButNoValidContentTest() {

        when(headerMock.getValue())
                .thenReturn("AX4");

        when(httpResponseMock.getFirstHeader("Retry-After"))
                .thenReturn(headerMock);

        // Holds the Result:
        OutParameter<Duration> result = new OutParameter<>();

        // Try to get the Result:
        boolean success = RetryHeaderUtils.tryDetermineRetryDelay(httpResponseMock, result);

        // Assertions:
        Assert.assertEquals(false, success);
    }

    @Test
    public void headerFoundWithSecondsContentTest() {

        when(headerMock.getValue())
                .thenReturn("4");

        when(httpResponseMock.getFirstHeader("Retry-After"))
                .thenReturn(headerMock);

        // Holds the Result:
        OutParameter<Duration> result = new OutParameter<>();

        // Try to get the Result:
        boolean success = RetryHeaderUtils.tryDetermineRetryDelay(httpResponseMock, result);

        // Assertions:
        Assert.assertEquals(true, success);
        Assert.assertEquals(4, result.get().getSeconds());
    }

    @Test
    public void headerFoundWithDateTimeInFutureContentTest() {

        // We assume the HTTP Header to contain an RFC1123-compliant DateTime value:
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

        String formattedStringInFuture = formatter.format(DateUtils.getUtcNow().plusYears(1));

        // Expectations
        when(headerMock.getValue())
                .thenReturn(formattedStringInFuture);

        when(httpResponseMock.getFirstHeader("Retry-After"))
                .thenReturn(headerMock);

        // Holds the Result:
        OutParameter<Duration> result = new OutParameter<>();

        // Try to get the Result:
        boolean success = RetryHeaderUtils.tryDetermineRetryDelay(httpResponseMock, result);

        // Assertions:
        Assert.assertEquals(true, success);
        Assert.assertNotEquals(0, result.get().getSeconds());
        Assert.assertTrue(result.get().getSeconds() > 120);
    }

    @Test
    public void headerFoundWithNegativeDateTimeContentTest() {

        when(headerMock.getValue())
                .thenReturn("Tue, 3 Jun 2008 11:05:30 GMT");

        when(httpResponseMock.getFirstHeader("Retry-After"))
                .thenReturn(headerMock);

        // Holds the Result:
        OutParameter<Duration> result = new OutParameter<>();

        // Try to get the Result:
        boolean success = RetryHeaderUtils.tryDetermineRetryDelay(httpResponseMock, result);

        // Assertions:
        Assert.assertEquals(true, success);
        Assert.assertEquals(0, result.get().getSeconds());
    }
}
