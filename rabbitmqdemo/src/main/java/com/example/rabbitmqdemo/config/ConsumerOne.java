package com.example.rabbitmqdemo.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 14:49
 * @since jdk1.8
 */
@Component
@Slf4j
public class ConsumerOne {

    private final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    private final CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private TaskExecutor taskExecutor;

    public ConsumerOne(CachingConnectionFactory cachingConnectionFactory, SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.simpleRabbitListenerContainerFactory = simpleRabbitListenerContainerFactory;
    }

//
//    /**
//     * 如果 acknowledgeMode 设置成None，就是设置了amqp协议里面的消费自动确认，amqp代理发送了消息之后就从队列删除消息
//     * @param content
//     * @throws InterruptedException
//     */
//    @RabbitHandler
//    @RabbitListener(queues = RabbitConfig.QUEUE_A, containerFactory = "rabbitListenerContainerFactory",ackMode = "NONE")
//    public void process(String content) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(5);
//        if (content.length() > 9) {
//            throw new RuntimeException("szdgd");
//        }
//        log.info("接受到队列={}的消息内容={}", RabbitConfig.QUEUE_A, content);
//    }


    /**
     * 如果 acknowledgeMode 设置成None，就是设置了amqp协议里面的消费自动确认，amqp代理发送了消息之后就从队列删除消息
     * 从spring-amqp 2.2开始，支持批量消费，一次接受一批消息
     *
     * @param content
     * @throws InterruptedException
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_A, containerFactory = "rabbitListenerContainerFactory")
    public void processBatch(List<Message> content, Channel channel,
                             @Headers Map<String, Object> map) throws InterruptedException, IOException {
//        TimeUnit.SECONDS.sleep(2);
        age:
        log.info("批量拉取消息={}", content.size());
        if (content.size() > 9) {
            throw new RuntimeException("szdgd");
        }
        log.info("接受到队列={}的消息内容={}", RabbitConfig.QUEUE_A, content);
        for (Message message : content) {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag, true);
        }

//        /**
//         * basicRecover方法是进行补发操作，
//         * 其中的参数如果为true是把消息退回到queue但是有可能被其它的consumer(集群)接收到，
//         * 设置为false是只补发给当前的consumer
//         */
//        channel.basicRecover(false);
    }


    /**
     * 全部使用注解搞定，不再声明队列，交换机，绑定
     * 直接接受对象，需要对象实现序列化接口
     * @throws InterruptedException
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "anno_queue", autoDelete = "true", durable = "false"),
            exchange = @Exchange(value = "anno_exchange", autoDelete = Exchange.TRUE, durable = Exchange.FALSE),
            key = "anno_routingkey"),
            containerFactory = "rabbitListenerContainerFactory")
    public void allAnnotation(@Payload User user, Channel channel,
                              @Headers Map<String, Object> map) throws InterruptedException, IOException {
//        TimeUnit.SECONDS.sleep(2);

        Object tag = map.get(AmqpHeaders.DELIVERY_TAG);
        if (tag != null) {
            channel.basicAck(Long.parseLong(tag.toString()),false);
        }
        log.info("接受到队列={}的消息内容={}", RabbitConfig.QUEUE_A, user);

    }
}
