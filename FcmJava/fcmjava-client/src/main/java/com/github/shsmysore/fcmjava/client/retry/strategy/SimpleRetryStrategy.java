// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.retry.strategy;

import com.github.shsmysore.fcmjava.client.functional.Action0;
import com.github.shsmysore.fcmjava.client.functional.Func1;
import com.github.shsmysore.fcmjava.exceptions.FcmRetryAfterException;

import java.time.Duration;

/**
 * The SimpleRetryStrategy retries all methods, that throw a @see {@link FcmRetryAfterException} for a
 * maximum number of retries.
 *
 * The @see {@link FcmRetryAfterException} includes a Retry Delay, which indicates when the method
 * should be retried. This Strategy waits for the amount of time given in the @see {@link FcmRetryAfterException}
 * and waits for a fixed amount of time.
 */
public class SimpleRetryStrategy implements IRetryStrategy {

    private final int maxRetries;

    public SimpleRetryStrategy(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public void doWithRetry(Action0 action) {
        getWithRetry(() -> {
            action.invoke();

            return null;
        });
    }

    @Override
    public <TResult> TResult getWithRetry(Func1<TResult> function) {

        // Holds the current Retry Count:
        int currentRetryCount = 0;

        // Holds the Return Value:
        TResult returnValue = null;

        // Simple Retry Loop with Thread Sleep for waiting:
        do {
            try {
                returnValue = function.invoke();
                // Break out of Loop, if there was no exception:
                break;
            } catch(FcmRetryAfterException e) {
                currentRetryCount = currentRetryCount + 1;
                // If we hit the maximum retry count, then throw the Exception:
                if(currentRetryCount == maxRetries) {
                    throw e;
                }
                // Sleep for the amount of time returned by FCM:
                internalSleep(e.getRetryDelay());
            }
        } while(currentRetryCount <= maxRetries);

        // And finally return the result:
        return returnValue;
    }

    private void internalSleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
