package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Account;
import com.fballfans.elasticsearch.service.impl.AccountSearchService;
import com.john.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangjuwa
 * @date 2019/4/24
 * @since jdk1.8
 **/
@RestController
@RequestMapping("account/search")
public class AccountSearchController {

    private final AccountSearchService accountSearchService;

    @Autowired
    public AccountSearchController(AccountSearchService accountSearchService) {
        this.accountSearchService = accountSearchService;
    }

    /**
     * 查询任何字段包含某个关键词所有文档
     *
     * @param pageable
     * @param keyword
     * @return
     */
    @GetMapping("key/{keyword}")
    public Result<Page<Account>> keyword(@PageableDefault(sort = {"balance"}) org.springframework.data.domain.Pageable pageable, @PathVariable("keyword") String keyword) {
        Page<Account> page = accountSearchService.keyword(pageable, keyword);
        return new Result<>(page);
    }

    /**
     * 指定字段来查找,term是完全匹配，不做分词处理
     * 通过性别和 state来查找
     *
     * @return
     */
    @GetMapping("term")
    public Result<Page<Account>> term(@PageableDefault(sort = {"balance"}) org.springframework.data.domain.Pageable pageable,
                                      @RequestParam(value = "gender", required = false) String gender,
                                      @RequestParam(value = "state", required = false) String state,
                                      @RequestParam(value = "address", required = false) String address) {
        Page<Account> page = accountSearchService.term(pageable, gender, state, address);
        return new Result<>(page);
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
     * @see {https://blog.csdn.net/tianyaleixiaowu/article/details/77965257}
     * @return
     */
    @GetMapping("phrase")
    public Result<Page<Account>> phrase(@PageableDefault(sort = {"balance"}) org.springframework.data.domain.Pageable pageable,
                                        @RequestParam(value = "phrase") String phrase) {
        Page<Account> page = accountSearchService.phrase(pageable, phrase);
        return new Result<>(page);
    }
}
