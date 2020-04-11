package com.example.rabbitmqdemo.controller;

import com.example.rabbitmqdemo.config.RabbitConfig;
import com.example.rabbitmqdemo.domain.entity.Order;
import com.example.rabbitmqdemo.domain.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ""
 * @description 制作一个kafka生产者消费者客户端，部署在同一个服务器上
 * @date 2018/9/25
 * @since jdk1.8
 */
@RestController
@RequestMapping("api/orders")
@Slf4j
public class OrderController {

    public static final String ORDER_LIST = "order_list";
    public static final String LOCK = ORDER_LIST + "_LOCK";
    public static final String TEST_7 = "test8";


    private final RabbitTemplate rabbitTemplate;


    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Serializable> redisTemplate;

    private final OrderRepository orderRepository;

    public OrderController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, RedisTemplate<String, Serializable> redisTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
    }


    @GetMapping("add")
    public Result<String> add(String goods_name, BigDecimal price) throws JsonProcessingException {
        Order order = new Order();
        order.setName(goods_name);
        order.setPrice(price);

        CorrelationData data = new CorrelationData();
        data.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, objectMapper.writeValueAsString(order), data);
        return new Result<>("订单创建中...");
    }

    @GetMapping("list")
    public Result<List<Order>> list() {

        List range = redisTemplate.opsForList().range(ORDER_LIST, 0, 10);
        if (CollectionUtils.isEmpty(range)) {
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(LOCK, 1);
            List<Order> content;
            try {
                if (BooleanUtils.isTrue(flag)) {
                    log.info("获取线程了");
                    Page<Order> orderPage = orderRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime")));
                    content = orderPage.getContent();
                    if (!content.isEmpty()) {
                        redisTemplate.opsForList().leftPushAll(ORDER_LIST, content.toArray(new Order[0]));
                    }
                } else {
                    return deferGetResult();
                }
            } finally {
                redisTemplate.expire(ORDER_LIST, 10, TimeUnit.SECONDS);
                redisTemplate.delete(LOCK);
            }

            return new Result<>(content);
        } else {
            return new Result<>(range);
        }
    }

    private Result<List<Order>> deferGetResult() {
        while (true) {
            redisTemplate.expire(LOCK, 10, TimeUnit.SECONDS);
            Serializable lock = redisTemplate.opsForValue().get(LOCK);
            if (lock == null) {
                break;
            }
        }
        List range = redisTemplate.opsForList().range(ORDER_LIST, 0, 10);
        return new Result<>(range);
    }
}
