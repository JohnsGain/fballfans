package com.example.rabbitmqdemo.controller;

import com.example.rabbitmqdemo.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class RabbitmqController {


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


    /**
     * @param content 测试 {@link org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback}
     *                当 spring.rabbitmq.template.mandatory 设置为true,发生的消息没有队列消费的时候，
     *                就会从交换机返回到 发送这者
     */
    @GetMapping("retureCallback")
    public void retureCallback(String content, @RequestParam(required = false) String exchange) {

        CorrelationData data = new CorrelationData();
        data.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange != null ? exchange : RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A + "阿萨德刚",
                content, data);
    }

    /**
     * 测试通过TTL 死信队列的方式来实现延迟消息消费
     */
    @GetMapping("delayMessage")
    public void delayMessage(long delay) {
        String content = "发生消息延时" + delay;
        CorrelationData data = new CorrelationData();
        data.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_EXCHANGE, RabbitConfig.DELAY_KEY,
                content, message -> {
                    //注意这里时间要是字符串形式,单位 毫秒
                    message.getMessageProperties().setExpiration(""+delay);
                    //发送到哪里，可以灵活 使用这个参数配置，在RPC实现里面指定回调队列
//                    message.getMessageProperties().setReplyTo("");
                    return message;

                });
    }


}
