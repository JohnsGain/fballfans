package com.fballfans.elasticsearch.repository;

import com.fballfans.elasticsearch.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
public interface ICountrySearchRepository extends ElasticsearchRepository<Country, Long> {

    /**
     * @param name
     * @return
     */
    @Nullable
    List<Country> findByName(@Nullable String name);

    //使用 Page<Country> countrys = countrySearchRepository.findByName("测试",  PageRequest.of(0, 10)); //分页是从0开始的
    Page<Country> findByName(String name, Pageable pageable);

}
