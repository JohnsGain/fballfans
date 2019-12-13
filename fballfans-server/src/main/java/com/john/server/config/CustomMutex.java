package com.john.server.config;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义实现  互斥锁
 * 可以看到Mutex将Lock接口均代理给了同步器的实现。
 * 使用方将Mutex构造出来之后，调用lock获取锁，调用unlock进行解锁。
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-13 10:35
 * @since jdk1.8
 */
public class CustomMutex implements Lock, Serializable {

    private final MySync sync;

    public CustomMutex() {
        sync = new MySync();
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.condition();
    }

    public boolean isLock() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    /**
     * 内部类，自定义同步器
     */
    private static class MySync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -2622157059362663883L;

        /**
         * @return 是否处于占用状态
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * @param acquires 当状态为0的时候获取锁
         * @return
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            assert acquires == 1;
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * @param releases 释放锁，将状态设置为0
         * @return
         */
        @Override
        protected boolean tryRelease(int releases) {
            assert releases == 1;
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setState(0);
            setExclusiveOwnerThread(null);
            return false;
        }

        /**
         * @return 返回一个Condition，每个condition都包含了一个condition队列
         */
        Condition condition() {
            return new ConditionObject();
        }
    }

}
