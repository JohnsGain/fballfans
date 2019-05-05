package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Post;
import com.fballfans.elasticsearch.repository.IPostRepository;
import com.john.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/4/25
 * @since jdk1.8
 **/
@RestController
@RequestMapping("post")
public class PostController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final IPostRepository postRepository;

    @Autowired
    public PostController(ElasticsearchTemplate elasticsearchTemplate, IPostRepository postRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.postRepository = postRepository;
    }

    /**
     * 单字段对某字符串模糊查询
     */
    @RequestMapping("/singleMatch")
    public Result<Page<Post>> singleMatch(String content, Integer userId, @PageableDefault Pageable pageable) {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("content", content)).withPageable(pageable).build();
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("userId", userId)).withPageable(pageable).build();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("content", content))
                .withPageable(pageable)
                .build();
        return new Result<>(postRepository.search(searchQuery));
    }

    /**
     * term匹配，即不分词匹配，你传来什么值就会拿你传的值去做完全匹配
     * 我们可以用这个来做那种需要==查询的操作，当传userId=1时，会查询出来所有userId为1的集合。
     * 通常情况下，我们不会使用term查询，绝大部分情况我们使用ES的目的就是为了使用它的分词模糊查询功能。
     * 而term一般适用于做过滤器filter的情况，譬如我们去查询title中包含“浣溪沙”且userId=1时，
     * 那么就可以用termQuery("userId", 1)作为查询的filter。
     */
    @RequestMapping("/singleTerm")
    @ApiOperation("查询某用户下 content 里面有某个词的文档")
    public Result<Page<Post>> singleTerm(Integer userId, @PageableDefault Pageable pageable, String content) {
        //不对传来的值分词，去找完全匹配的
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("content", content))
                .withFilter(QueryBuilders.termQuery("userId", userId))
                .withPageable(pageable)
                .build();
        Page<Post> search = postRepository.search(searchQuery);
        return new Result<>(search);
    }

    /**
     * 如果我们希望title，content两个字段去匹配某个字符串，只要任何一个字段包括该字符串即可，就可以使用multimatch。
     *
     * @param userId
     * @param pageable
     * @param content
     * @return
     */
    @RequestMapping("multimatch")
    @ApiOperation("查询某用户下 content或者title 里面有某个词的文档")
    public Result<Page<Post>> multimatch(@RequestParam(required = false) Integer userId, @PageableDefault Pageable pageable,
                                         String content, String type) {
        //不对传来的值分词，去找完全匹配的
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(content, "content", "title")
                        .type(MultiMatchQueryBuilder.Type.valueOf(type)))
                .withPageable(pageable);
        if (userId != null) {
            queryBuilder.withFilter(QueryBuilders.termQuery("userId", userId));
        }
        Page<Post> search = postRepository.search(queryBuilder.build());
        return new Result<>(search);
    }

    /**
     * 查询content必须包含输入参数分词之后所有词汇的文档,
     * 要指定 operator为 and（就是参数分词之后包含所有词汇的文档才可以） ，默认是or(就是参数分词之后包含任一词汇的文档都可以)
     *
     * @param userId
     * @param pageable
     * @param content
     * @return
     */
    @RequestMapping("contains")
    @ApiOperation("查询content必须包含输入参数分词之后所有词汇的文档")
    public Result<Page<Post>> contains(@RequestParam(required = false) Integer userId, @PageableDefault Pageable pageable,
                                       String content) {
        //不对传来的值分词，去找完全匹配的
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("content", content)
                        .operator(Operator.AND)
                        .minimumShouldMatch("20%"))
                .withPageable(pageable);
        if (userId != null) {
            queryBuilder.withFilter(QueryBuilders.termQuery("userId", userId));
        }
        Page<Post> search = postRepository.search(queryBuilder.build());
        return new Result<>(search);
    }

    @ApiOperation("查询 content 包含“XXX”，且userId=“1”，且weight最好小于5的结果。那么就可以使用boolQuery来组合。")
    @GetMapping("bool")
    public Result<Page<Post>> bool(@RequestParam(required = false) Integer userId, @PageableDefault Pageable pageable,
                                   String content) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (userId != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("userId", userId));
        }
        if (StringUtils.isNotBlank(content)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("content", content));
        }
        boolQueryBuilder.should(QueryBuilders.rangeQuery("weight")
                .lte(5)).minimumShouldMatch(1);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        return new Result<>(postRepository.search(searchQuery));
    }

    @ApiOperation("查询给定用户id集合的所有文档")
    @GetMapping("terms")
    public Result<Page<Post>> terms(@RequestParam List<Integer> userIds, @PageableDefault Pageable pageable) {
        TermsQueryBuilder userId = QueryBuilders.termsQuery("userId", userIds);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(userId)
                .withPageable(pageable)
                .build();
        Page<Post> search = postRepository.search(searchQuery);
        return new Result<>(search);
    }

}
