package com.john.server.test;

import com.john.server.domain.entity.SysAcl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:21
 * @since jdk1.8
 */
@RestController
@RequestMapping("trx")
public class TransactionController {

    @Autowired
    private TestService testService;


    @GetMapping("t1")
    public void t1(String remark) throws InterruptedException {
        testService.t1(remark);
    }
    @GetMapping("t1")
    public void tt2(String remark) throws InterruptedException {
        testService.tt2(remark);
    }
    @GetMapping("t12")
    public SysAcl t12() {
        return testService.get();
    }
}
