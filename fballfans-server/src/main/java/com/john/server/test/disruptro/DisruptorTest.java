package com.john.server.test.disruptro;

import com.john.server.FballfansServerApplication;
import com.john.server.disruptor.DisruptorMqService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhangjuwa
 * @date 2020/7/15
 * @since jdk1.8
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FballfansServerApplication.class)
public class DisruptorTest {

    @Autowired
    private DisruptorMqService disruptorMqService;

    @Autowired
    TaskExecutor taskExecutor;

    /**
     * 项目内部使用Disruptor做消息队列
     *
     * @throws Exception
     */
    @Test
    public void sayHelloMqTest() throws Exception {
        for (int i = 0; i < 200; i++) {
           taskExecutor.execute(()-> disruptorMqService.sayHelloMq("消息到了，Hello world!"));
        }
        log.info("消息队列已发送完毕");
        //这里停止2000ms是为了确定是处理消息是异步的
        Thread.sleep(20000);
    }
}
