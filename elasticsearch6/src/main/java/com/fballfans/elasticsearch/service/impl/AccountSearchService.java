package com.fballfans.elasticsearch.service.impl;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.repository.IAccountRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/4/24
 * @since jdk1.8
 **/
@Service
public class AccountSearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final IAccountRepository accountRepository;

    @Autowired
    public AccountSearchService(ElasticsearchTemplate elasticsearchTemplate, IAccountRepository accountRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.accountRepository = accountRepository;
    }

    public Page<Account> keyword(org.springframework.data.domain.Pageable pageable, String keyword) {
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(QueryBuilders.queryStringQuery(keyword))
                .build();
        return accountRepository.search(searchQuery);
    }

    public Page<Account> term(Pageable pageable, String gender, String state, String address) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(gender)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("gender", gender));
        }
        if (StringUtils.isNotBlank(state)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("state", state));
        }
        if (StringUtils.isNotBlank(address)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("address", address));
        }
        //多字段排序
        Sort sort = new Sort(Sort.Direction.DESC, "balance");
        sort.and(new Sort(Sort.Direction.DESC, "age"));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
        return accountRepository.search(searchQuery);
    }

    /**
     * slop参数告诉match_phrase查询词条能够相隔多远时仍然将文档视为匹配。相隔多远的意思是，你需要移动一个词条多少次来让查询和文档匹配？
     * 我们以一个简单的例子来阐述这个概念。
     *
     * @param pageable
     * @param phrase
     * @return
     */
    public Page<Account> phrase(org.springframework.data.domain.Pageable pageable, String phrase) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(QueryBuilders.matchPhraseQuery("address", phrase).slop(5))
                .build();

        return accountRepository.search(searchQuery);
    }

    public List<Account> match(String address) {
        return Lists.newArrayList(accountRepository.search(QueryBuilders.matchQuery("address", address)));
    }
}
