package com.fballfans.elasticsearch.repository;

import com.fballfans.elasticsearch.entity.Account;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Repository
public interface IAccountRepository extends ElasticsearchRepository<Account, Long> {
}
