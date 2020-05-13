package com.john.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author zhangjuwa
 * @apiNote
 * @Date 2019-11-12 17:13
 * @since jdk1.8
 */
@SpringBootApplication
@EnableWebSocket
@EnableScheduling()
public class FballfansServerApplication {

    public static void main(String[] args) {
        //xx XX
        SpringApplication.run(FballfansServerApplication.class, args);
    }

    /**
     * @return 这个bean的创建要在  @EnableScheduling()  这个注解的解析之前完成
     */
    @Bean
    @Primary
    public TaskScheduler defaultSockJsTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("SockJS-");
        threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }
}
