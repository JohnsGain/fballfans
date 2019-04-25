package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.service.impl.AccountServiceImpl;
import com.john.Pageable;
import com.john.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author zhangjuwa
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

    /**
     * 准备数据，在所有文档里面加入一个经纬度
     *
     * @return
     */
    @PutMapping
    public Result<String> geo() {
        accountService.geo();
        return new Result<>("success");
    }

    @PostMapping()
    public Result<Account> add(Account account) {
        account = accountService.save(account);
        return new Result<>(account);
    }

    @GetMapping("page")
    public Result<Page<Account>> page(Pageable pageable, @RequestParam(value = "firstname", required = false) String firstname,
                                      @RequestParam(value = "state", required = false) String state) {
        Page<Account> page = accountService.page(pageable, firstname, state);
        return new Result<>(page);
    }

    @GetMapping("or")
    public Result<Page<Account>> or(Pageable pageable, @RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "address2", required = false) String address2) {
        Page<Account> page = accountService.or(pageable, address, address2);
        return new Result<>(page);
    }

    @GetMapping("and")
    public Result<Page<Account>> and(Pageable pageable, @RequestParam(value = "address", required = false) String address,
                                     @RequestParam(value = "address2", required = false) String address2) {
        Page<Account> page = accountService.and(pageable, address, address2);
        return new Result<>(page);
    }

    @GetMapping("between")
    public Result<Page<Account>> betweent(Pageable pageable, @RequestParam(value = "from", required = false) BigDecimal from,
                                          @RequestParam(value = "to", required = false) BigDecimal to) {
        Page<Account> page = accountService.betweent(pageable, from, to);
        return new Result<>(page);
    }

    @GetMapping("not")
    public Result<Page<Account>> not(Pageable pageable, @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountService.not(pageable, address);
        return new Result<>(page);
    }

    @GetMapping("lte")
    public Result<Page<Account>> not(Pageable pageable, @RequestParam(value = "bound", required = false) BigDecimal bound) {
        Page<Account> page = accountService.lte(pageable, bound);
        return new Result<>(page);
    }

    @GetMapping("like")
    public Result<Page<Account>> like(Pageable pageable, @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountService.like(pageable, address);
        return new Result<>(page);
    }

    @GetMapping("startwith")
    public Result<Page<Account>> startwith(Pageable pageable, @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountService.startwith(pageable, address);
        return new Result<>(page);
    }

    @GetMapping("in")
    public Result<Page<Account>> in(Pageable pageable, @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountService.in(pageable, address);
        return new Result<>(page);
    }

    @GetMapping("contain")
    public Result<Page<Account>> contain(Pageable pageable, @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountService.contain(pageable, address);
        return new Result<>(page);
    }

    /**
     * 根据地址查询并分页
     *
     * @param pageable
     * @param address
     * @return
     */
    @GetMapping("address/{address}")
    public Result<Page<Account>> address(Pageable pageable, @PathVariable(value = "address", required = false) String address) {
        Page<Account> page = accountService.address(pageable, address);
        return new Result<>(page);
    }


//    /**
//     * 余额最多的5个账户
//     * @return
//     */
//    @GetMapping("top5")
//    public Result<List<Account>> top5() {
//
//    }
}
