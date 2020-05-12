package com.example.rabbitmqdemo.config;

import com.example.rabbitmqdemo.domain.entity.Order;
import com.example.rabbitmqdemo.domain.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.example.rabbitmqdemo.config.RabbitConfig.QUEUE_B;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 14:49
 * @since jdk1.8
 */
@Component
@Slf4j
public class ConsumerTwo {


    private final ObjectMapper objectMapper;

    private final OrderRepository orderRepository;

    public ConsumerTwo(ObjectMapper objectMapper, OrderRepository orderRepository) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
    }

    /**
     * @param content
     * @param tag     这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel.
     *                如果确认模式是 AcknowledgeMode.MANUAL 手动，需要使用这个参数进行消息确认,
     */
    @RabbitHandler
    @RabbitListener(queues = QUEUE_B)
    public void process(String content, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                        @Headers Map<String, Object> map) throws IOException {
        log.info("接受到队列={}的消息内容={}", QUEUE_B, content);

        //创建订单，支付，减库存
        try {
            Order order = objectMapper.readValue(content, Order.class);
            orderRepository.save(order);
            //手动消息确认，如果是true,会进行批量确认，把包含tag在内的所有小于tag的消息都进行确认
            channel.basicAck(tag, false);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
//                    也可以拒绝该消息，消息会被丢弃，不会重回队列
            channel.basicReject((Long) map.get(AmqpHeaders.DELIVERY_TAG), false);
        }

    }



}
