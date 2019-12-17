package com.john.server.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对 @see {@link TwinsLock} 测试
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-17 15:54
 * @since jdk1.8
 */
public class TwinsLockTest {

    public static void main(String[] args) {
        //这种模式创建的线程，任务队列最大值Integer.maxVALUE,容易造成任务队列放入过多任务内存溢出
//        Executor executor = Executors.newFixedThreadPool(5);

        //这种模式创建的线程  最大线程数 Integer.MAX_VALUE，任务队列是 同步任务队列，容易造成创建过多线程内存溢出
//        Executor executor = Executors.newCachedThreadPool();
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), new BasicThreadFactory.Builder()
                .priority(5).namingPattern("test-poo-%d").daemon(false).build(), new ThreadPoolExecutor.AbortPolicy());

        executorService.execute(new TaskDmoe());
        executorService.execute(new TaskDmoe());
        executorService.execute(new TaskDmoe());
        executorService.execute(new TaskDmoe());
        executorService.execute(new TaskDmoe());
        executorService.execute(new TaskDmoe());
    }

}

class TaskDmoe implements Runnable {

    static Logger log = LoggerFactory.getLogger(TaskDmoe.class);

    static TwinsLock twinsLock = new TwinsLock();

    @Override
    public void run() {
        log.warn("3523");
        if (twinsLock.tryLock()) {
            log.warn("进啦==");
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("没有");
        }
    }
}