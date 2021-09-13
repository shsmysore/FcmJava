// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.requests.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shsmysore.fcmjava.requests.builders.NotificationPayloadBuilder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationPayload {
    private final String title;
    private final String body;
    private final String icon;
    private final String sound;
    private final String badge;
    private final String tag;
    private final String color;
    private final String clickAction;
    private final String bodyLocKey;
    private final List<String> bodyLocKeyArgs;
    private final String titleLocKey;
    private final List<String> titleLocKeyArgs;
    private final String androidChannelId;

    public NotificationPayload(String title, String body, String icon, String sound, String badge, String tag, String color, String clickAction, String bodyLocKey, List<String> bodyLocKeyArgs, String titleLocKey, List<String> titleLocKeyArgs, String androidChannelId) {
        this.title = title;
        this.body = body;
        this.icon = icon;
        this.sound = sound;
        this.badge = badge;
        this.tag = tag;
        this.color = color;
        this.clickAction = clickAction;
        this.bodyLocKey = bodyLocKey;
        this.bodyLocKeyArgs = bodyLocKeyArgs;
        this.titleLocKey = titleLocKey;
        this.titleLocKeyArgs = titleLocKeyArgs;
        this.androidChannelId = androidChannelId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

    @JsonProperty("sound")
    public String getSound() {
        return sound;
    }

    @JsonProperty("badge")
    public String getBadge() {
        return badge;
    }

    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    @JsonProperty("click_action")
    public String getClickAction() {
        return clickAction;
    }

    @JsonProperty("body_loc_key")
    public String getBodyLocKey() {
        return bodyLocKey;
    }

    @JsonProperty("body_loc_args")
    public List<String> getBodyLocKeyArgs() {
        return bodyLocKeyArgs;
    }

    @JsonProperty("title_loc_key")
    public String getTitleLocKey() {
        return titleLocKey;
    }

    @JsonProperty("title_loc_args")
    public List<String> getTitleLocKeyArgs() {
        return titleLocKeyArgs;
    }

    @JsonProperty("android_channel_id")
    public String getAndroidChannelId() {
        return androidChannelId;
    }

    public static NotificationPayloadBuilder builder() {
        return new NotificationPayloadBuilder();
    }
}
