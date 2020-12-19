package com.john.server.controller;

import com.john.server.service.TransactionService2;
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
@RequestMapping("trx2")
public class TransactionDemo2Controller {

    @Autowired
    private TransactionService2 transactionService2;

    @GetMapping("atomic")
    public void atomic() throws InterruptedException {
        transactionService2.atomic();
    }

    @GetMapping("atomic2")
    public void atomic2() throws InterruptedException {
        transactionService2.atomic2();
    }
}
