package com.john.server.service;

import com.john.server.domain.entity.SysRole;
import com.john.server.domain.repository.SysRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * mysql数据库加事务并不会对数据加锁，另一个事务同样可以对同一条记录进行写操作，
 * 这种情况会造成更新丢失，后提交的事务把先提交的事务对同一条数据的更新覆盖了。
 * <p>
 * 如果希望更改某一条记录的时候，不让另一个事务获取到这条记录，需要显示加上写锁，
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-13 09:42
 * @since jdk1.8
 */
@Service
@Slf4j
public class TransactionService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 演示可重复读隔离级别下，幻读发生
     *
     * @throws InterruptedException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void rr() throws InterruptedException {
        //如果要防止其他事务并发读取当前记录，要加锁
        List<SysRole> byOperator = sysRoleRepository.findByOperator("1");

        log.info("书数据量={}", byOperator.size());

        //睡眠期间，另一个线程插入一条新的 数据
        TimeUnit.SECONDS.sleep(20);

        SysRole sysRole = new SysRole();
        sysRole.setOperator("2");
        sysRoleRepository.save(sysRole);

        //睡眠期间，另一个线程插入一条新的 数据
        TimeUnit.SECONDS.sleep(5);

        //下面for update的读是当前读，一定能读到最新的数据，就是另一个事务提交之后的数据
        byOperator = sysRoleRepository.findByOperatorForUpdate("1");
        log.info("书数据量={}", byOperator.size());

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void gap() throws InterruptedException {
        List<SysRole> roleList = sysRoleRepository.findByIdBetween(2L, 8L);
        roleList.forEach(item -> {
            item.setName(RandomStringUtils.random(5, "ROLE_ADMIN"));
            sysRoleRepository.save(item);
        });
        //睡眠期间，另一个线程插入一条新的 数据
        TimeUnit.SECONDS.sleep(20);
        SysRole sysRole = sysRoleRepository.findById(6L).orElse(null);
        System.out.println(sysRole);
    }

    /**
     * @param sysRole
     * @param sleep
     * @throws InterruptedException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void add(SysRole sysRole, int sleep) throws InterruptedException {
        log.info("进来这里");
//        SysRole sysRole1 = sysRoleRepository.findByIdLock((long) 6);
        SysRole sysRole1 = sysRoleRepository.findById(5L).orElse(null);
        log.info("读取到数据");
        if (sysRole1 == null) {
            return;
        }
        sysRole1.setRemark("XXXXXX" + RandomStringUtils.random(4, "1234567890"));
        sysRoleRepository.save(sysRole1);
        log.info("更新完了");
        //睡眠期间，另一个线程插入一条新的 数据
        TimeUnit.SECONDS.sleep(sleep);
        log.info("返回");
    }

//    @Scheduled(cron = "*/5 * * * * ?")
//    public void sendMessage() throws IOException {
//        WebSocketServer.sendInfo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), null);
//    }


}
