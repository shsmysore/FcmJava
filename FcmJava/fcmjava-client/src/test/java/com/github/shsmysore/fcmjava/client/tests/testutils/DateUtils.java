// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.tests.testutils;

import java.time.*;
import java.util.Date;

public class DateUtils {
    public static Date from(LocalDate localDate, LocalTime localTime, ZoneOffset zoneOffset) {
        LocalDateTime localDateTime = localDate.atTime(localTime);

        return from(localDateTime, zoneOffset);
    }

    public static Date from(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

        return Date.from(offsetDateTime.toInstant());
    }

}
