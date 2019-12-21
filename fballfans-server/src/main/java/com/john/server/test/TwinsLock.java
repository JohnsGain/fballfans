package com.john.server.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 利用 @see {@link java.util.concurrent.locks.AbstractQueuedSynchronizer}来自定义实现一个
 * 同步工具。一次允许两个线程获取锁并发访问，超过就进入阻塞状态
 * <p>
 * 对于这个需求，可以利用同步器完成一个这样的设定，定义一个初始状态，为2，一个线程进行获取那么减1，一个线程释放那么加1，
 * 状态正确的范围在[0，1，2]三个之间，当在0时，代表再有新的线程对资源进行获取时只能进入阻塞状态（注意在任何时候进行状态
 * 变更的时候均需要以CAS作为原子性保障）。由于资源的数量多于1个，同时可以有两个线程占有资源，因此需要实现tryAcquireShared
 * 和tryReleaseShared方法，这里谢谢luoyuyou和同事小明指正，已经修改了实现。
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-17 15:07
 * @since jdk1.8
 */
public class TwinsLock implements Lock {

    private final TwinsSync sync;

    public TwinsLock() {
        sync = new TwinsSync(2);
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private static class TwinsSync extends AbstractQueuedSynchronizer {

        TwinsSync(int state) {
            if (state < 0) {
                throw new IllegalStateException("count must large than zero.");
            }
            setState(state);
        }

        /**
         * @param arg 共享模式获取
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
            assert arg == 1;
            for (; ; ) {
                int count = getState();
                int newCount = count - 1;
                if (newCount < 0 || compareAndSetState(count, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            assert arg == 1;
            for (; ; ) {
                int count = getState();
                int newCount = count + 1;
                if (compareAndSetState(count, newCount)) {
                    return true;
                }
            }
        }

    }
}
