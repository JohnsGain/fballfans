package com.example.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.ListenerContainerIdleEvent;
import org.springframework.context.ApplicationListener;

/**
 * 消费者很长时间没收到消息，就会触发这个事件，配置闲置时长，spring.rabbitmq.listener.simple.idle-event-interval=5000
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-31 11:54
 * @since jdk1.8
 */
//@Component
@Slf4j
public class MyListenerContainerIdleEventListenerImpl implements ApplicationListener<ListenerContainerIdleEvent> {


    @Override
    public void onApplicationEvent(ListenerContainerIdleEvent event) {
        log.info("消费者已经限制很久了。。。。");
    }
}
