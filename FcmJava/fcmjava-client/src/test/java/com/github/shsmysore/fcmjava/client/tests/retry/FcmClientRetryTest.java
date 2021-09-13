// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.tests.retry;

import com.github.shsmysore.fcmjava.client.FcmClient;
import com.github.shsmysore.fcmjava.client.http.IHttpClient;
import com.github.shsmysore.fcmjava.client.retry.RetryUtils;
import com.github.shsmysore.fcmjava.client.tests.testutils.TestUtils;
import com.github.shsmysore.fcmjava.exceptions.FcmRetryAfterException;
import com.github.shsmysore.fcmjava.http.client.IFcmClient;
import com.github.shsmysore.fcmjava.http.constants.HttpStatus;
import com.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import com.github.shsmysore.fcmjava.model.builders.FcmMessageOptionsBuilder;
import com.github.shsmysore.fcmjava.requests.groups.CreateDeviceGroupMessage;
import com.github.shsmysore.fcmjava.responses.CreateDeviceGroupMessageResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FcmClientRetryTest {

    @Mock
    private IFcmClientSettings settingsMock;

    @Mock
    private IHttpClient httpClientMock;

    @Before
    public void Setup() {
        initMocks(this);
    }

    @Test
    public void retryWithThrowTest() {

        // Fake Message to send:
        CreateDeviceGroupMessage createDeviceGroupMessage = new CreateDeviceGroupMessage(new FcmMessageOptionsBuilder().build(), new ArrayList<>(), "Unit Test");

        when(httpClientMock.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class))
                .thenThrow(new FcmRetryAfterException(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError", Duration.ZERO));

        // Create the Test Subject:
        IFcmClient client = new FcmClient(settingsMock, httpClientMock);

        // Invoke it and make sure it throws:
        TestUtils.assertThrows(() -> RetryUtils.getWithRetry(() -> client.send(createDeviceGroupMessage), 5), FcmRetryAfterException.class);

        // And finally verify it has been called 5 times as set in the Mock Expectations:
        verify(httpClientMock, times(5))
                .post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class);
    }

    @Test
    public void retryNotNecessaryTest() {

        // Fake Message to send:
        CreateDeviceGroupMessage createDeviceGroupMessage = new CreateDeviceGroupMessage(new FcmMessageOptionsBuilder().build(), new ArrayList<>(), "Unit Test");

        // Fake Message to receive:
        CreateDeviceGroupMessageResponse createDeviceGroupMessageResponse = new CreateDeviceGroupMessageResponse("Unit Test");

        when(httpClientMock.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class))
                .thenReturn(createDeviceGroupMessageResponse);

        // Create the Test Subject:
        IFcmClient client = new FcmClient(settingsMock, httpClientMock);

        // Invoke it and make sure it throws:
        TestUtils.assertDoesNotThrow(() -> RetryUtils.getWithRetry(() -> client.send(createDeviceGroupMessage), 5));

        // And finally verify it has been called 5 times as set in the Mock Expectations:
        verify(httpClientMock, times(1))
                .post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class);
    }
}
