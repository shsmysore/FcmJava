// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.model.options;


import io.github.shsmysore.fcmjava.model.builders.FcmMessageOptionsBuilder;
import io.github.shsmysore.fcmjava.model.enums.PriorityEnum;

public class FcmMessageOptions {

    private final String condition;
    private final String collapseKey;
    private final PriorityEnum priorityEnum;
    private final Boolean contentAvailable;
    private final Boolean delayWhileIdle;
    private final int timeToLive;
    private final String restrictedPackageName;
    private final Boolean dryRun;
    private final Boolean mutableContent;

    public FcmMessageOptions(String condition, String collapseKey, PriorityEnum priorityEnum, Boolean contentAvailable, Boolean delayWhileIdle, int timeToLive, String restrictedPackageName, Boolean dryRun, Boolean mutableContent) {
        this.condition = condition;
        this.collapseKey = collapseKey;
        this.priorityEnum = priorityEnum;
        this.contentAvailable = contentAvailable;
        this.delayWhileIdle = delayWhileIdle;
        this.timeToLive = timeToLive;
        this.restrictedPackageName = restrictedPackageName;
        this.dryRun = dryRun;
        this.mutableContent = mutableContent;
    }

    public String getCondition() {
        return condition;
    }

    public String getCollapseKey() {
        return collapseKey;
    }

    public PriorityEnum getPriorityEnum() {
        return priorityEnum;
    }

    public Boolean getContentAvailable() {
        return contentAvailable;
    }

    public Boolean getDelayWhileIdle() {
        return delayWhileIdle;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public String getRestrictedPackageName() {
        return restrictedPackageName;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public Boolean getMutableContent() {
        return mutableContent;
    }

    public static FcmMessageOptionsBuilder builder() {
        return new FcmMessageOptionsBuilder();
    }
}
