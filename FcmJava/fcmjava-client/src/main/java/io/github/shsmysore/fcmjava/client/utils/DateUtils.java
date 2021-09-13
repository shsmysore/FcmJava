package io.github.shsmysore.fcmjava.client.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtils {

    /**
     * Gets the current UTC DateTime.
     *
     * @return Current UTC DateTime
     */
    public static ZonedDateTime getUtcNow() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

}
