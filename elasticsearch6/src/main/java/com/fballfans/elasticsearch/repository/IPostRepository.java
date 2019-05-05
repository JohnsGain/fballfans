package com.fballfans.elasticsearch.repository;

import com.fballfans.elasticsearch.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhangjuwa
 * @date 2019/4/25
 * @since jdk1.8
 **/
public interface IPostRepository extends ElasticsearchRepository<Post, String> {
}
