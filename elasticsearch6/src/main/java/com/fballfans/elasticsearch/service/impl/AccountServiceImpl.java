package com.fballfans.elasticsearch.service.impl;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.repository.IAccountRepository;
import com.fballfans.elasticsearch.service.IAccountService;
import com.john.Pageable;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Page<Account> page(Pageable pageable, String firstname, String state) {

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(pageable.getPage(), pageable.getSize()));
//                .withQuery(QueryBuilders.matchAllQuery());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        QueryStringQueryBuilder.
        if (StringUtils.isNotBlank(state)) {
            TermQueryBuilder state1 = QueryBuilders.termQuery("state", state);
            boolQueryBuilder.must(state1);
        }
        if (StringUtils.isNotBlank(firstname)) {
            TermQueryBuilder firstname1 = QueryBuilders.termQuery("firstname", firstname);
            boolQueryBuilder.must(firstname1);
        }
        searchQuery.withFilter(boolQueryBuilder);
        return accountRepository.search(searchQuery.build());
    }


    public Page<Account> address(Pageable pageable, String address) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddress(address, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> or(Pageable pageable, String address, String address2) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressOrAddress(address, address2,
                PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> and(Pageable pageable, String address, String address2) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressAndAddress(address, address2,
                PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> betweent(Pageable pageable, BigDecimal from, BigDecimal to) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByBalanceBetween(from.doubleValue(), to.doubleValue(),
                PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> not(Pageable pageable, String address) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressNot(address, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> lte(Pageable pageable, BigDecimal bound) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByBalanceLessThanEqual(bound.doubleValue(), PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> like(Pageable pageable, String address) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressLike(address, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    /**
     * @param pageable
     * @param address
     * @return
     */
    public Page<Account> startwith(Pageable pageable, String address) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressStartingWith(address, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> in(Pageable pageable, String address) {
        List<String> strings = Stream.of(address.split(",")).collect(Collectors.toList());
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressIn(strings, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Page<Account> contain(Pageable pageable, String address) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        return accountRepository.findByAddressContaining(address, PageRequest.of(pageable.getPage(), pageable.getSize(), sort));
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void geo() {
        Iterable<Account> all = accountRepository.findAll();
        double latitude = 23;
        double longitude = 100;
        List<IndexQuery> list = new ArrayList<>();
        for (Account account : all) {
            account.setId((account.getAccount_number() == null) ? 1111 : account.getAccount_number());
            GeoPoint geoPoint = new GeoPoint(latitude + RandomUtils.nextDouble(0, 1),
                    longitude + RandomUtils.nextDouble(0, 2));
            account.setGeoPoint(geoPoint);
            IndexQuery build = new IndexQueryBuilder()
                    .withType("_doc")
                    .withId(account.getId().toString())
                    .withIndexName("bank")
                    .withObject(account)
                    .build();
            list.add(build);
        }
        elasticsearchTemplate.bulkIndex(list);
        elasticsearchTemplate.refresh(Account.class);
    }

    @Override
    public String toString() {
        return "AccountServiceImpl{" +
                "accountRepository=" + accountRepository +
                '}';
    }
}
