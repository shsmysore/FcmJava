package io.github.shsmysore.fcmjava.requests.builders;

import io.github.shsmysore.fcmjava.requests.notification.NotificationPayload;

import java.util.List;

/**
 * Builder for creating {@link NotificationPayload} instances.
 *
 * All fields are optional, and some of them are common for both Android and iOS and some
 * of them are specific to Android ({@link #icon}, {@link #tag}, {@link #color})
 * or specific to iOS ({@link #badge}).
 *
 * @author Francisco Aranda (fran.culebras@gmail.com)
 */
public class NotificationPayloadBuilder {

    private String title;
    private String body;
    private String icon;
    private String sound;
    private String badge;
    private String tag;
    private String color;
    private String clickAction;
    private String bodyLocKey;
    private List<String> bodyLocKeyArgs;
    private String titleLocKey;
    private List<String> titleLocKeyArgs;
    private String androidChannelId;

    public NotificationPayloadBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationPayloadBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public NotificationPayloadBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public NotificationPayloadBuilder setSound(String sound) {
        this.sound = sound;
        return this;
    }

    public NotificationPayloadBuilder setBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public NotificationPayloadBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public NotificationPayloadBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public NotificationPayloadBuilder setClickAction(String clickAction) {
        this.clickAction = clickAction;
        return this;
    }

    public NotificationPayloadBuilder setBodyLocKey(String bodyLocKey) {
        this.bodyLocKey = bodyLocKey;
        return this;
    }

    public NotificationPayloadBuilder setBodyLocKeyArgs(List<String> bodyLocKeyArgs) {
        this.bodyLocKeyArgs = bodyLocKeyArgs;
        return this;
    }

    public NotificationPayloadBuilder setTitleLocKey(String titleLocKey) {
        this.titleLocKey = titleLocKey;
        return this;
    }

    public NotificationPayloadBuilder setTitleLocKeyArgs(List<String> titleLocKeyArgs) {
        this.titleLocKeyArgs = titleLocKeyArgs;
        return this;
    }

    public NotificationPayloadBuilder setAndroidChannelId(String androidChannelId) {
        this.androidChannelId = androidChannelId;
        return this;
    }

    public NotificationPayload build() {
        return new NotificationPayload(
                title,
                body,
                icon,
                sound,
                badge,
                tag,
                color,
                clickAction,
                bodyLocKey,
                bodyLocKeyArgs,
                titleLocKey,
                titleLocKeyArgs,
                androidChannelId);
    }

}
