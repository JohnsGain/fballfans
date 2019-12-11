package com.john.server.test;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-10 17:04
 * @since jdk1.8
 */
public class MySchedulingConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        ScheduledExecutorService
//        TaskScheduler t=new ConcurrentTaskScheduler()
//        scheduledTaskRegistrar.setTaskScheduler();
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler());

    }

    @Bean
    public TaskScheduler taskScheduler() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2,
                new BasicThreadFactory.Builder().daemon(true).namingPattern("test-pool-%d").priority(5).build(),
                new ThreadPoolExecutor.AbortPolicy());
//        JdbcTemplateLockProvider
        return new ConcurrentTaskScheduler(executorService);
    }
}
