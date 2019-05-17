package com.john.auth.service;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @date 2019/5/14
 * @since jdk1.8
 */
@Component
public class RedisDistributedLockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributedLockService.class);

    private final RedisTemplate<String, Serializable> redisTemplate;

    private static TransmittableThreadLocal<Long> key = new TransmittableThreadLocal<>();

    private static final String KEY = "key";

    @Autowired
    public RedisDistributedLockService(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 1.由于这个方法不是原子的，如果里面执行了set方法之后，服务挂点，后面的expire方法没有执行，那么这个key就是永久有效的了，造成死锁
     * 2.目前bbd贵阳项目redis都是采用cluster分布式集群，不支持multi操作， MULTI is currently not supported in cluster mode.
     *
     * @param times
     * @return
     */
    @Deprecated
    public boolean getMutexLock(int times) {
        if (times > 3) {
            return false;
        }
        long nanoTime = System.nanoTime();

        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        long expireTime = 600L * 1000;
        //由于这个方法不是原子的，如果里面执行了set方法之后，服务挂点，后面的expire方法没有执行，那么这个key就是永久有效的了，造成死锁
        Boolean aBoolean = opsForValue.setIfAbsent(KEY, nanoTime, expireTime, TimeUnit.MILLISECONDS);
        if (aBoolean != null && aBoolean) {
            key.set(nanoTime);
            return true;
        } else {
            try {
                Long expire = redisTemplate.getExpire(KEY, TimeUnit.MICROSECONDS);
                expire = (expire == null) ? 0 : expire;
                TimeUnit.MICROSECONDS.sleep(expireTime - expire - RandomUtils.nextLong(0, 10));
            } catch (InterruptedException e) {
                //可能线程睡眠失败，如果这种情况发生，就继续去获取锁
                LOGGER.warn("线程中断异常", e);
            }
            return getMutexLock(++times);
        }
    }


    /**
     * 这里get 和delete不是原子操作，如果执行了get方法之后，这个key过期，那么另一个线程可能就会获取到锁并且set新的key，下面就会删除其他线程的设置的value,
     * 然后，第三个线程又会获取到锁，导致同步锁失效
     */
    @Deprecated
    public void releaseLock() {
        String oldvalue = (String) redisTemplate.opsForValue().get(KEY);
        Long aLong = key.get();
        if (oldvalue != null && oldvalue.equals(aLong.toString())) {
            redisTemplate.delete(KEY);
        }
    }

}
