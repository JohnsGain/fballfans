package com.john.server.test;

import com.john.server.domain.entity.SysAcl;
import com.john.server.domain.repository.SysAclRepository;
import com.john.server.domain.repository.SysRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:16
 * @since jdk1.8
 */
@Slf4j
@Service
public class TestService {

    @Autowired
    private SysAclRepository sysAclRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void t1(String remark) throws InterruptedException {

        SysAcl sysAcl = sysAclRepository.findById((long) 2).orElse(null);
        if (sysAcl != null) {
            sysAcl.setRemark(remark);
            sysAclRepository.save(sysAcl);
            SysAcl sysAcl1 = sysAclRepository.findById(2L).get();
            log.info("结果={}", sysAcl1.getRemark());
        }
        log.info("睡觉");
        TimeUnit.SECONDS.sleep(60);
        log.info("睡觉结束..");
    }

    @Transactional
    public SysAcl get() {
        SysAcl sysAcl = sysAclRepository.findById((long) 2).orElse(null);
        return sysAcl;
    }

}
