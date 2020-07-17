package com.john.server.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjuwa
 * @date 2020/7/15
 * @since jdk1.8
 */
@Configuration
@Slf4j
public class DisruptorConfig {

    @Bean("messageModel")
    public RingBuffer<MessageModel> messageModelRingBuffer() {
        log.info("配置=={}", "messageModel");
        //指定事件工厂
        HelloEventFactory factory = new HelloEventFactory();

      /*  指定ringbuffer字节大小，必须为2的N次方（能将求模运算转为位运算提高效率），否则将影响效率。
        bufferSize有多大，就会循环遍历创建多少个 消息对象到ringbuffer里面。这也是为什么disruptor高效的一个原因，
        在消息非常多的情况下，可以减少大量消息创建的时间浪费，但是另一方面就是需要占用更多的空间来存储消息对象
        */
        int bufferSize = 1024;

        //单线程模式，获取额外的性能
        BasicThreadFactory.Builder builder = new BasicThreadFactory.Builder().namingPattern("disruptor-factory-%d");
        Disruptor<MessageModel> disruptor = new Disruptor<>(factory, bufferSize, builder.build(),
                ProducerType.MULTI, new BlockingWaitStrategy());

        //设置事件业务处理器---消费者
        disruptor.handleEventsWith(new HelloEventHandler());

        // 启动disruptor线程
        disruptor.start();

        //获取ringbuffer环，用于接取生产者生产的事件
        RingBuffer<MessageModel> ringBuffer = disruptor.getRingBuffer();

        return ringBuffer;
    }
}
