package com.john.server.disruptor;

/**
 * @author zhangjuwa
 * @date 2020/7/15
 * @since jdk1.8
 */
public interface DisruptorMqService {
    /**
     * 消息
     * @param message
     */
    void sayHelloMq(String message);
}
