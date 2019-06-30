package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.repository.IAccountRepository;
import com.fballfans.elasticsearch.service.impl.AccountSearchService;
import com.john.Result;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
     * 指定字段来查找,term是完全匹配，不做分词处理,且传入的参数不经过过滤器，分词器等分析，
     * 如果address="Mill street",输入参数为Mill,得不到结果，因为在es里面的数据研究分析过变成小写了，
     * 参数的Mill没有分析，直接在es里面就找不到与之匹配的结果
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
     * 某字段按字符串分词查询，会做分词处理
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
                .distance(1000, DistanceUnit.METERS);
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

    /**
     * 常用术语查询
     * 1. 该common术语查询是一个现代的替代提高了精确度和搜索结果的召回（采取禁用词进去），在不牺牲性能的禁用词。
     * 2. 查询中的每个术语都有成本。搜索"The brown fox" 需要三个术语查询，每个查询一个"the"，"brown"并且 "fox"所有查询都针对索引中的所有文档执行。
     * 查询"the"可能与许多文档匹配，因此对相关性的影响比其他两个术语小得多。
     * <p>
     * 以前，这个问题的解决方案是忽略高频率的术语。通过将其"the"视为停用词，我们减少了索引大小并减少了需要执行的术语查询的数量。
     * 这种方法的问题在于，虽然停用词对相关性的影响很小，但它们仍然很重要。如果我们删除了停用词，我们就会失去精确度
     * （例如，我们无法区分"happy" 和"not happy"），并且我们会失去回忆（例如，文本在索引中"The The"或者 "To be or not to be"根本不存在）
     * 3.现在的解决方案
     * 该common术语的查询将所述查询术语分为两组：更重要（即低频率而言）和不太重要的（即，高频率而言这将先前已停用词）。
     * 首先，它搜索与更重要的术语匹配的文档。这些术语出现在较少的文档中，对相关性有较大影响。
     * 然后，它对不太重要的术语执行第二次查询 - 这些术语经常出现并且对相关性的影响很小。但是，它不是计算所有匹配文档的相关性分数，
     * 而是仅计算_score已经与第一个查询匹配的文档。通过这种方式，高频项可以改善相关性计算，而无需支付性能不佳的成本。
     *
     * @param text
     * @return
     * @see { 参考文档 https://blog.csdn.net/ctwy291314/article/details/82836514}
     */
    @GetMapping("commonterms")
    public Result<List<Account>> commonterms(String text) {
        CommonTermsQueryBuilder address = QueryBuilders.commonTermsQuery("address", text);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(address)
                .build();
        return new Result<>(elasticsearchTemplate.queryForList(searchQuery, Account.class));
    }

    /**
     * moreLikeThisQuery: 实现基于内容推荐, 支持实现一句话相似文章查询
     * {
     * "more_like_this" : {
     * "fields" : ["title", "content"],   // 要匹配的字段, 不填默认_all
     * "like_text" : "text like this one",   // 匹配的文本
     * }
     * }
     * <p>
     * percent_terms_to_match：匹配项（term）的百分比，默认是0.3
     * <p>
     * min_term_freq：一篇文档中一个词语至少出现次数，小于这个值的词将被忽略，默认是2
     * <p>
     * max_query_terms：一条查询语句中允许最多查询词语的个数，默认是25
     * <p>
     * stop_words：设置停止词，匹配时会忽略停止词
     * <p>
     * min_doc_freq：一个词语最少在多少篇文档中出现，小于这个值的词会将被忽略，默认是无限制
     * <p>
     * max_doc_freq：一个词语最多在多少篇文档中出现，大于这个值的词会将被忽略，默认是无限制
     * <p>
     * min_word_len：最小的词语长度，默认是0
     * <p>
     * max_word_len：最多的词语长度，默认无限制
     * <p>
     * boost_terms：设置词语权重，默认是1
     * <p>
     * boost：设置查询权重，默认是1
     * <p>
     * analyzer：设置使用的分词器，默认是使用该字段指定的分词器
     */
    @ApiOperation("模糊查询")
    @GetMapping("morelike")
    public Result<List<Account>> morelike(String text) {
        // 要匹配的字段, 不填默认_all
        MoreLikeThisQueryBuilder address = QueryBuilders.moreLikeThisQuery(new String[]{"address", "state"});
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(address)
                .build();
        return new Result<>(elasticsearchTemplate.queryForList(searchQuery, Account.class));
    }

    /**
     * 包裹查询, 高于设定分数, 不计算相关性
     *
     * @param text
     * @param pageable
     * @return
     */
    @ApiOperation("包裹查询")
    @GetMapping("constantscore")
    public Result<Page<Account>> constantScore(String text,
                                               @PageableDefault(sort = {"balance"}, direction = Sort.Direction.DESC) Pageable pageable) {
        ConstantScoreQueryBuilder address = QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery("address", text))
                .boost(2.0f);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(address)
                .withPageable(pageable)
                .build();
        return new Result<>(elasticsearchTemplate.queryForPage(searchQuery, Account.class));
    }

    /**
     * 1.通配符查询, 支持 * 匹配任何字符序列, 包括空
     * 2.避免* 开始, 会检索大量内容造成效率缓慢
     * 3.单个字符用?
     *
     * @param wildcard
     * @return
     */
    @ApiOperation("通配符查询")
    @GetMapping("wildcard")
    public Result<List<Account>> wildcardQuery(String wildcard) {
        WildcardQueryBuilder address = QueryBuilders.wildcardQuery("address", wildcard);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(address)
                .build();
        return new Result<>(elasticsearchTemplate.queryForList(searchQuery, Account.class));
    }

    /**
     * scroll查询原理是在第一次查询的时候一次性生成一个快照，根据上一次的查询的id来进行下一次的查询，这个就类似于关系型数据库的游标，
     * 然后每次滑动都是根据产生的游标id进行下一次查询，这种性能比上面说的分页性能要高出很多，基本都是毫秒级的。 注意：scroll不支持跳页查询。
     * 使用场景：对实时性要求不高的查询，例如微博或者头条滚动查询。
     *
     * @return
     */
    @GetMapping("scroll")
    public Result scroll() {

        return null;
    }

    /**
     * 查询Id集合对应的文档
     *
     * @param ids
     * @return
     */
    @GetMapping("idsquery")
    public Result<List<Account>> idsquery(@RequestParam List<String> ids) {
        String[] arr = new String[ids.size()];
        arr = ids.toArray(arr);
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery()
                .addIds(arr);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("bank")
                .withFilter(idsQueryBuilder).build();
        List<Account> accounts = elasticsearchTemplate.queryForList(searchQuery, Account.class);
        return new Result<>(accounts);
    }

    /**
     * 如果指定的字段是string类型，模糊查询是基于编辑距离算法来匹配文档。编辑距离的计算基于我们提供的查询词条和被搜索文档。如果指定的字段是数值类型或者日期类型，
     * 模糊查询基于在字段值上进行加减操作来匹配文档（The fuzzy query uses similarity based on Levenshtein edit distance for  fields, and a
     * margin on numeric and date fields）。
     *
     * @param text
     * @return
     * @apiNote 此查询很占用CPU资源，但当需要模糊匹配时它很有用，例如，当用户拼写错误时。
     * <p>
     * fuzzyQuery 基于编辑距离 (Levenshtein) 来进行相似搜索, 比如搜索 kimzhy, 可以搜索出 kinzhy(编辑距离为 1)
     * 为了进行测试说明, 前创建一个索引, 插入几条数据 ka,kab,kib,ba, 我们的搜索源为 ki
     * 了解更多关于编辑距离 (Levenshtein) 的概念, 请参考:<a href='http://www.cnblogs.com/biyeymyhjob/archive/2012/09/28/2707343.html'></a>
     * 了解更多编辑距离的算法, 请参考:<a href='http://blog.csdn.net/ironrabbit/article/details/18736185'></a>
     * ki — ka 编辑距离为 1
     * ki — kab 编辑距离为 2
     * ki — kbia 编辑距离为 3
     * ki — kib 编辑距离为 1
     * 所以当我们设置编辑距离 (ES 中使用 fuzziness 参数来控制) 为 0 的时候, 没有结果
     * 所以当我们设置编辑距离 (ES 中使用 fuzziness 参数来控制) 为 1 的时候, 会出现结果 ka,kib
     * 所以当我们设置编辑距离 (ES 中使用 fuzziness 参数来控制) 为 2 的时候, 会出现结果 ka,kib,kab
     * 所以当我们设置编辑距离 (ES 中使用 fuzziness 参数来控制) 为 3 的时候, 会出现结果 ka,kib,kab,kbaa(很遗憾,ES 本身最多只支持到 2, 因此不会出现此结果)
     * @apiNote prefixLength 最小前缀不参与模糊匹配的字符长度,例如设置为1，当传入参数road的时候，第一个字符就不会参与
     * 模糊匹配，需要一定匹配，从后面开始进行模虎匹配
     */
    @ApiOperation("模糊查询")
    @GetMapping("fuzzy")
    public Result<Page<Account>> fuzzy(String field, String text, @PageableDefault(sort = {"balance"}) Pageable pageable, @RequestParam(defaultValue = "0") Integer prefix) {
        FuzzyQueryBuilder address = QueryBuilders.fuzzyQuery(field, text)
                //模糊匹配的最大编辑距离(指两个字串之间，由一个转成另一个所需的最少编辑操作次数)
                .fuzziness(Fuzziness.ONE)
                //最小前缀不参与模糊匹配的字符长度，不传参数就是0
                .prefixLength(prefix)
//        控制最大的返回结果
//                .maxExpansions(100)
                ;
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withFilter(address).build();
        Page<Account> accounts = elasticsearchTemplate.queryForPage(searchQuery, Account.class);
        return new Result<>(accounts);
    }

    /**
     * 对查询的文档要满足特定的前缀字符串，对输入参数不进行analyzer，意味着对参数不分词，不小写等等
     *
     * @param field
     * @param prefix
     * @param pageable
     * @return
     */
    @ApiOperation("前缀查询")
    @GetMapping("prefixquery")
    public Result<Page<Account>> prefixquery(String field, String prefix, @PageableDefault(sort = {"balance"}) Pageable pageable) {
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(field, prefix);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withFilter(prefixQueryBuilder).build();
        Page<Account> accounts = elasticsearchTemplate.queryForPage(searchQuery, Account.class);
        return new Result<>(accounts);
    }

    @ApiOperation("正则表达式查询")
    @GetMapping("regexquery")
    public Result<Page<Account>> regexquery(String field, String regex, @PageableDefault(sort = {"balance"}) Pageable pageable) {
        RegexpQueryBuilder flags = QueryBuilders.regexpQuery(field, regex)
                //设置支持的正则表达式范围，默认支持所有正则表达式
                .flags(RegexpFlag.ALL);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withFilter(flags).build();
        Page<Account> accounts = elasticsearchTemplate.queryForPage(searchQuery, Account.class);

        return new Result<>(accounts);
    }


}
