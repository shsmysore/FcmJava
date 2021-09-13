// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.http.apache.utils;

import com.github.shsmysore.fcmjava.client.utils.DateUtils;
import com.github.shsmysore.fcmjava.client.utils.OutParameter;
import com.github.shsmysore.fcmjava.client.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RetryHeaderUtils {

    public static boolean tryDetermineRetryDelay(HttpResponse httpResponse, OutParameter<Duration> result) {
        try {
            return internalTryDetermineRetryDelay(httpResponse, result);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean internalTryDetermineRetryDelay(HttpResponse httpResponse, OutParameter<Duration> result) {

        // Try to get the Retry-After Header send by FCM:
        Header retryAfterHeader = httpResponse.getFirstHeader("Retry-After");

        // Early exit, if we do not have a Retry Header:
        if (retryAfterHeader == null) {
            return false;
        }

        // Try to get the Value:
        String retryDelayAsString = retryAfterHeader.getValue();

        // Early exit, if the Retry Header has no Value:
        if(StringUtils.isNullOrWhiteSpace(retryDelayAsString)) {
            return false;
        }

        // First check if we have a Number Retry Delay as Seconds:
        if(tryGetFromLong(retryDelayAsString, result)) {
            return true;
        }

        // Then check if we have a RFC1123-compliant date:
        if(tryGetFromDate(retryDelayAsString, result)) {
            return true;
        }

        return false;
    }

    private static boolean tryGetFromLong(String retryDelayAsString, OutParameter<Duration> result) {

        // Try to convert the String to a Long:
        OutParameter<Long> longResult = new OutParameter<>();

        if(!tryConvertToLong(retryDelayAsString, longResult)) {
            return false;
        }

        // If we can convert it to Long, then convert to a Duration in seconds:
        Duration retryDelayAsDuration = Duration.ofSeconds(longResult.get());

        // Set in the Out Parameter:
        result.set(retryDelayAsDuration);

        return true;
    }

    private static boolean tryConvertToLong(String longAsString, OutParameter<Long> result) {
        try {
            result.set(Long.parseLong(longAsString));

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private static boolean tryGetFromDate(String dateAsString, OutParameter<Duration> result) {

        // Try to convert the String to a RFC1123-compliant Zoned DateTime
        OutParameter<ZonedDateTime> resultDate = new OutParameter<>();

        if(!tryToConvertToDate(dateAsString, resultDate)) {
            return false;
        }

        // Get the UTC Now DateTime and the Retry DateTime in UTC Time Zone:
        ZonedDateTime utcNowDateTime = DateUtils.getUtcNow();
        ZonedDateTime nextRetryDateTime = resultDate.get().withZoneSameInstant(ZoneOffset.UTC);

        // Calculate Duration between both as the Retry Delay:
        Duration durationToNextRetryTime = Duration.between(utcNowDateTime, nextRetryDateTime);

        // Negative Retry Delays should not be allowed:
        if(durationToNextRetryTime.getSeconds() < 0) {
            durationToNextRetryTime = Duration.ofSeconds(0);
        }

        // Set it as Result:
        result.set(durationToNextRetryTime);

        // And return success:
        return true;
    }

    private static boolean tryToConvertToDate(String dateAsString, OutParameter<ZonedDateTime> result) {
        try {

            // We assume the HTTP Header to contain an RFC1123-compliant DateTime value:
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

            // Try to parse and set it as the result:
            result.set(ZonedDateTime.parse(dateAsString, formatter));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
