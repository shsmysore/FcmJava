// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.test.model;

import io.github.shsmysore.fcmjava.model.topics.Topic;
import io.github.shsmysore.fcmjava.model.topics.TopicList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TopicListTest {

    @Test
    public void testTopicName() {

        Topic topic0 = new Topic("topic0");
        Topic topic1 = new Topic("topic1");

        TopicList topicList = new TopicList(Arrays.asList(topic0, topic1));

        Assert.assertEquals("'topic0' in topics || 'topic1' in topics", topicList.getTopicsCondition());

        Assert.assertEquals("topic0", topicList.getTopics().get(0).getName());
        Assert.assertEquals("topic1", topicList.getTopics().get(1).getName());
    }
}
