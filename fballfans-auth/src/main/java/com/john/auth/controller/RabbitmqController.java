package com.john.auth.controller;

import com.john.auth.properties.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 23:22
 * @since jdk1.8
 */
@RestController
@RequestMapping("mq")
@Slf4j
public class RabbitmqController implements RabbitTemplate.ConfirmCallback {

    private final RabbitTemplate rabbitTemplate;

    public RabbitmqController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("send")
    public void sent(String content) {
        CorrelationData data = new CorrelationData();
        data.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, content, data);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + cause);
        }
    }

}
