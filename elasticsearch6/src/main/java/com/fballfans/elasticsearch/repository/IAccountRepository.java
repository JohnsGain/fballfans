package com.fballfans.elasticsearch.repository;

import com.fballfans.elasticsearch.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Repository
public interface IAccountRepository extends ElasticsearchRepository<Account, Long> {

    Page<Account> findByAddress(@Nullable String address, Pageable pageable);

    Page<Account> findByAddressNot(@Nullable String address, Pageable pageable);

    Page<Account> findByAddressOrAddress(@Nullable String address, @Nullable String address2, Pageable pageable);

    Page<Account> findByAddressAndAddress(@Nullable String address, @Nullable String address2, Pageable pageable);

    Page<Account> findByBalanceBetween(Double from, @Nullable Double to, Pageable pageable);

    Page<Account> findByBalanceLessThanEqual(Double bound, Pageable pageable);

    /**
     * 左右模糊匹配 address like *xxx*
     * @param bound
     * @param pageable
     * @return
     */
    Page<Account> findByAddressLike(String bound, Pageable pageable);

    Page<Account> findByAddressStartingWith(String start, Pageable pageable);

    Page<Account> findByAddressIn(Collection<String> address, Pageable pageable);

    Page<Account> findByAddressContaining(String address, Pageable pageable);

}
