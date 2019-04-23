package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.service.impl.AccountServiceImpl;
import com.john.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountServiceImpl accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{id}")
    public Result<Account> findById(@PathVariable("id") Long id) {
        Account account = accountService.findById(id);
        return new Result<>(account);
    }
}
