package com.fballfans.elasticsearch.service.impl;

import com.fballfans.elasticsearch.entity.Country;
import com.fballfans.elasticsearch.repository.ICountrySearchRepository;
import com.john.Pageable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Service
public class CountryServiceImpl {

    private final ICountrySearchRepository countrySearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public CountryServiceImpl(ICountrySearchRepository countrySearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.countrySearchRepository = countrySearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Country findById(Long id) {
        return countrySearchRepository.findById(id).orElse(null);
    }

    public Country save(Country country) {
        elasticsearchTemplate.putMapping(Country.class);
        Optional<Country> byId = countrySearchRepository.findById(country.getId());
        byId.ifPresent((item) -> {
            throw new RuntimeException("这个id的文当已经存在");
        });
        country = countrySearchRepository.save(country);
        return country;
    }

    public List<Country> findByName(String name) {
        return countrySearchRepository.findByName(name);
    }

    public Page<Country> page(Pageable pageable, String name) {
        if (StringUtils.isNotBlank(name)) {
            return countrySearchRepository.findByName(name, PageRequest.of(pageable.getPage(), pageable.getSize()));
        } else {
            return countrySearchRepository.findAll(PageRequest.of(pageable.getPage(), pageable.getSize()));
        }
    }

    public void deleteById(Long id) {
        countrySearchRepository.deleteById(id);
    }

    public Country update(Country country) {
        country = countrySearchRepository.save(country);
        return country;
    }
}
