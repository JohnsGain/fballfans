package com.john.server.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author zhangjuwa
 * @date 2020/7/15
 * @since jdk1.8
 */
public class HelloEventFactory implements EventFactory<MessageModel> {
    @Override
    public MessageModel newInstance() {
        return new MessageModel();
    }
}
