package com.fballfans.elasticsearch.service.impl;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.repository.IAccountRepository;
import com.fballfans.elasticsearch.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Service
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public AccountServiceImpl(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
