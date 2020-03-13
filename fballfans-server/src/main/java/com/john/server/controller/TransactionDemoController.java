package com.john.server.controller;

import com.john.server.domain.entity.SysRole;
import com.john.server.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示事务
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-13 09:40
 * @since jdk1.8
 */
@RestController
@Slf4j
@RequestMapping("trx")
public class TransactionDemoController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("rr")
    public String rr() throws InterruptedException {
        transactionService.rr();
        return "success";
    }

    @GetMapping("add")
    public String add(SysRole sysRole,Long id) {
        sysRole = new SysRole();
        sysRole.setOperator("1");
        sysRole.setId(id);
        transactionService.add(sysRole);
        return "success";

    }

    @GetMapping("gap")
    public String gap() throws InterruptedException {
        transactionService.gap();
        return "success";
    }

}
