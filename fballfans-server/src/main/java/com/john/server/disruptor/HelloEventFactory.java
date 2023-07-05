package com.john.server.disruptor;

import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangjuwa
 * @date 2020/7/15
 * @since jdk1.8
 */
@Slf4j
public class HelloEventFactory implements EventFactory<MessageModel> {
    @Override
    public MessageModel newInstance() {
//        log.info("哪里在调用....一直在创建对象");
        return new MessageModel();
    }
}
