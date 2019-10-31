package com.john.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.TimeZone;

/**
 * 测试手动发起定时任务
 * @author zhangjuwa
 * @date 2019/9/17
 * @since jdk1.8
 */
@Service
@Slf4j
public class TaskSchedulerService {

    private final TaskScheduler taskScheduler;

    @Autowired
    public TaskSchedulerService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void taskScheduler() throws NoSuchMethodException {
        Method print = this.getClass().getMethod("print");
        Runnable runnable = new ScheduledMethodRunnable(this, print);
        CronTask cronTask = new CronTask(runnable, new CronTrigger("0/1 * * * * ?", TimeZone.getTimeZone("GMT+8")));
        long d = 1000_000_234;
        taskScheduler.schedule(runnable, cronTask.getTrigger());
    }

    public void print() {
        log.info("手动定时任务匹配====");
    }
}
