package com.example.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 确认消息发送成功，通过实现ConfirmCallBack接口，消息发送到交换器Exchange后触发回调
 *
 * 实现ReturnCallback接口，如果消息从交换器发送到对应队列失败时触发
 * # （比如根据发送消息时指定的routingKey找不到队列时会触发）
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-31 12:42
 * @since jdk1.8
 */
@Slf4j
@Component
public class ConfirmCallBackListenerImpl implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功发送");
        } else {
            log.info("消息发送失败:" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("没找到队列消费消息={},routekey={},exchange={}",message,routingKey,exchange);
    }
}
