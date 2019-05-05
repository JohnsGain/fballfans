package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.repository.IAccountRepository;
import com.fballfans.elasticsearch.service.impl.AccountSearchService;
import com.john.Result;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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
 * @date 2019/4/24
 * @since jdk1.8
 **/
@RestController
@RequestMapping("account/search")
public class AccountSearchController {

    private final IAccountRepository accountRepository;

    private final AccountSearchService accountSearchService;

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public AccountSearchController(AccountSearchService accountSearchService, ElasticsearchTemplate elasticsearchTemplate, IAccountRepository accountRepository) {
        this.accountSearchService = accountSearchService;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.accountRepository = accountRepository;
    }

    /**
     * 查询任何字段包含某个关键词所有文档
     *
     * @param pageable
     * @param keyword
     * @return
     */
    @GetMapping("key")
    public Result<Page<Account>> keyword(@PageableDefault(sort = {"balance"}) Pageable pageable, @RequestParam("keyword") String keyword) {
        Page<Account> page = accountSearchService.keyword(pageable, keyword);
        return new Result<>(page);
    }

    /**
     * 指定字段来查找,term是完全匹配，不做分词处理,
     * 通过性别和 state来查找
     *
     * @return
     */
    @GetMapping("term")
    public Result<Page<Account>> term(@PageableDefault(sort = {"balance"}) org.springframework.data.domain.Pageable pageable,
                                      @RequestParam(value = "gender", required = false) String gender,
                                      @RequestParam(value = "state", required = false) String state,
                                      @RequestParam(value = "address", required = false) String address) {
        //不对传来的值分词，去找完全匹配的
        Page<Account> page = accountSearchService.term(pageable, gender, state, address);
        return new Result<>(page);
    }

    /**
     * 某字段按字符串模糊查询，会做分词处理
     *
     * @param address
     * @return
     */
    @GetMapping("match")
    public Result<List<Account>> match(@RequestParam(value = "address", required = false) String address) {
        List<Account> list = accountSearchService.match(address);
        return new Result<>(list);
    }

    /**
     * 短语匹配
     * 单字段对某短语进行匹配查询，短语分词的顺序会影响结果
     * note: match查询类似，match_phrase查询首先解析查询字符串来产生一个词条列表。然后会搜索所有的词条，
     * 但只保留包含了所有搜索词条的文档，并且词条的位置要邻接。一个针对短语“中华共和国”的查询不会匹配“中华人民共和国”，
     * 因为没有含有邻接在一起的“中华”和“共和国”词条。
     * 这种完全匹配比较严格，类似于数据库里的“%落日熔金%”这种，使用场景比较狭窄。如果我们希望能不那么严格，譬如搜索“中华共和国”，
     * 希望带“我爱中华人民共和国”的也能出来，就是分词后，中间能间隔几个位置的也能查出来，可以使用slop参数。
     * <p>
     * match_phrase
     *
     * @return
     * @see {https://blog.csdn.net/tianyaleixiaowu/article/details/77965257}
     */
    @GetMapping("phrase")
    public Result<Page<Account>> phrase(@PageableDefault(sort = {"balance"}) org.springframework.data.domain.Pageable pageable,
                                        @RequestParam(value = "phrase") String phrase) {
        Page<Account> page = accountSearchService.phrase(pageable, phrase);
        return new Result<>(page);
    }

    @ApiOperation("查询某文档地址附近100米范围的文档")
    @GetMapping("geodistance")
    public Result<List<Account>> geodistance(Double latitude, Double longitude) {
//        QueryBuilders.geoShapeQuery()
        //查询某经纬度100米范围内
        GeoDistanceQueryBuilder geoPoint1 = QueryBuilders.geoDistanceQuery("geoPoint")
                .point(latitude, longitude)
                .distance(10000, DistanceUnit.METERS);
        GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("geoPoint", latitude, longitude)
                .unit(DistanceUnit.METERS)
                .order(SortOrder.ASC);
        NativeSearchQuery build = new NativeSearchQueryBuilder().withFilter(geoPoint1)
                .withSort(sortBuilder).build();
        List<Account> accounts = elasticsearchTemplate.queryForList(build, Account.class);
        return new Result<>(accounts);
    }

    @ApiOperation("查询某个地址点的文档")
    @GetMapping("geopoint")
    public Result<List<Account>> geopoint(Double latitude, Double longitude) {
        List<Account> accounts = accountRepository.findByGeoPoint(new GeoPoint(latitude, longitude));
        return new Result<>(accounts);
    }

    @ApiOperation("查询某文档地址附近100米范围的文档")
    @GetMapping("geopointwith")
    public Result<Page<Account>> geopointwith(Double latitude, Double longitude,
                                              @PageableDefault Pageable pageable, String distance) {
        Page<Account> accounts = accountRepository.findByGeoPointWithin(new GeoPoint(latitude, longitude),
                distance, pageable);
        return new Result<>(accounts);
    }

    @ApiOperation("查询余额在某个范围的文档")
    @GetMapping("range")
    public Result<List<Account>> range(double min, double max) {
        //1.使用gte和lte   gt,lt

//        QueryBuilders.rangeQuery("balance")
//                .gte(min)
//                .lte(max);

        //2. 或者使用from,to
        RangeQueryBuilder balance = QueryBuilders.rangeQuery("balance");
        balance.from(min)
                .to(max)
                .includeLower(true)
                .includeUpper(true);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(balance)
                .withSort(SortBuilders.fieldSort("balance").order(SortOrder.DESC))
                .build();
        List<Account> accounts = elasticsearchTemplate.queryForList(searchQuery, Account.class);
        return new Result<>(accounts);
    }

}
