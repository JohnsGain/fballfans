package com.fballfans.elasticsearch.service.impl;

import com.fballfans.elasticsearch.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangjuwa
 * @date 2019/4/26
 * @since jdk1.8
 **/
@Service
public class IndexCrudService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public IndexCrudService(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public boolean exist(@NonNull String indexName) {
        return elasticsearchTemplate.indexExists(indexName);
    }

    public void createIndex(@NonNull String indexName) {
        elasticsearchTemplate.createIndex(indexName);

        elasticsearchTemplate.deleteIndex(indexName);
    }

    public void index(@NonNull String indexName) {
        Account account = new Account();
        account.setId(1111L);
        account.setAccount_number(1111L);
        account.setAge(30);
        account.setState("GD");
        account.setCity("深圳市");
        account.setBalance(BigDecimal.valueOf(2100L));
        IndexQuery build = new IndexQueryBuilder()
                .withId(account.getId().toString())
                .withIndexName(indexName)
                .withObject(account).build();
        elasticsearchTemplate.index(build);
    }

    /**
     * 前面讲过JPA的save方法也可以save（List）批量插值，但适用于小数据量，要完成超大数据的插入就要用ES自带的bulk了，可以迅速插入百万级的数据。
     * 在ElasticSearchTemplate里也提供了对应的方法
     * @param accounts
     */
    public void bulk(List<Account> accounts, String indexName) {
        List<IndexQuery> list = new ArrayList<>();
        for (Account account : accounts) {
            IndexQuery build = new IndexQueryBuilder()
                    .withObject(account)
                    .withIndexName(indexName)
                    .withType(account.getClass().getSimpleName().toLowerCase())
                    .withId(account.getId().toString())
                    .build();
            list.add(build);
            if (list.size() % 1000 == 0) {
                elasticsearchTemplate.bulkIndex(list);
                list.clear();
            }
        }
        if (list.size() > 0) {
            elasticsearchTemplate.bulkIndex(list);
        }

    }
}
