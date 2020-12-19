package com.john.server.service;

import com.john.server.domain.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020/11/5 23:14
 * @since jdk1.8
 */
@Service
@Slf4j
public class TransactionService2 {

    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 测试update语句的原子性
     */
    @Transactional
    public void atomic() throws InterruptedException {
        log.info("打印日志");
        sysUserRepository.updateAge(2);
        TimeUnit.SECONDS.sleep(20);
    }


    /**
     * 测试update语句的原子性
     */
    @Transactional
    public void atomic2() throws InterruptedException {
        log.info("打印日志");
        sysUserRepository.updateAge(3);
        TimeUnit.SECONDS.sleep(5);
    }

}
