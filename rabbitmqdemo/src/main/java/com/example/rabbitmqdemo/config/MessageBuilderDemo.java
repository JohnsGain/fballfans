package com.example.rabbitmqdemo.config;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-31 14:53
 * @since jdk1.8
 */
public class MessageBuilderDemo {

    /**
     * rabbitmq中构建用于生产者发送的message的builder
     */
    @Test
    public void build() {
        Message message = MessageBuilder.withBody("foo".getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setMessageId("123")
                .setHeader("bar", "baz")
                .build();

        Map<String,Object> keyValue=new HashMap<>(16);
        Message build = MessageBuilder.withBody("asdfg".getBytes())
                .setAppId("ASDF")
                .setMessageId(UUID.randomUUID().toString())
                .setHeaderIfAbsent("NAME", "JOhn")
                .copyHeaders(keyValue)
                .build();
    }

    @Test
    public void properties() {
        Map<String,Object> keyValue=new HashMap<>(16);
        MessageProperties build = MessagePropertiesBuilder.newInstance().copyHeadersIfAbsent(keyValue)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("UTF-8")
                .setMessageId(UUID.randomUUID().toString())
                .build();

        Message build1 = MessageBuilder.withBody("sdfgasdg".getBytes())
                .andProperties(build)
                .build();
    }

}
