package com.example.rabbitmqdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 12:46
 * @since jdk1.8
 */
@Configuration
@Slf4j
public class RabbitConfig implements BeanPostProcessor {

    private final RabbitProperties rabbitProperties;


    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";
    public static final String EXCHANGE_C = "my-mq-exchange_C";

    public static final String DELAY_EXCHANGE = "delay_exchange";

    public static final String RECEIVE_EXCHANGE = "receive_exchange";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    public static final String DELAY_QUEUE = "delay_queue";
    public static final String RECEIVE_QUEUE = "receive_queue";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";

    public static final String DELAY_KEY = "delay_key";
    public static final String RECEIVE_KEY = "receive_key";

    @Autowired
    private ConfirmCallBackListenerImpl confirmCallBackListener;

    @Autowired
    private ObjectMapper objectMapper;

    public RabbitConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    /**
     * 死信接收交换机
     *
     * @return
     */
    @Bean
    public DirectExchange receiveExchange() {
        return new DirectExchange(RECEIVE_EXCHANGE);
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", RECEIVE_EXCHANGE);
        map.put("x-dead-letter-routing-key", RECEIVE_KEY);
        return new Queue(DELAY_QUEUE, true, false, false, map);
    }

    /**
     * 死信接收队列
     *
     * @return
     */
    @Bean
    public Queue receiveQueue() {
        return new Queue(RECEIVE_QUEUE);
    }

    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true);
    }

    /**
     * 给死信队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_KEY);
    }
    /**
     * 死信接收队列绑定 死信接收交换机
     * @return
     */
    @Bean
    public Binding receiveBinding(){
        return BindingBuilder.bind(receiveQueue()).to(receiveExchange()).with(RECEIVE_KEY);
    }

    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(queueA()).to(directExchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(directExchange()).with(ROUTINGKEY_B);
    }

    /**
     * 启用批量消费
     *
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean(name = "rabbitListenerContainerFactory")
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple",
            matchIfMissing = true)
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
//        BatchMessagingMessageListenerAdapter
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
//        ContentTypeDelegatingMessageConverter 这个转换器可以作全局转换器，它里面可以包含多个转换器
        //        factory.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        //启用批量消费
        factory.setConsumerBatchEnabled(true);
        factory.setBatchListener(true);

        return factory;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (RabbitTemplate.class.isAssignableFrom(bean.getClass())) {
            RabbitTemplate rabbitTemplate = (RabbitTemplate) bean;
            rabbitTemplate.setConfirmCallback(confirmCallBackListener);
            rabbitTemplate.setReturnCallback(confirmCallBackListener);
            return rabbitTemplate;
        }
        return bean;
    }

}
