package com.john.auth.controller;

import com.john.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @email 1047994907@qq.com
 * @date 2019/2/13
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@RestController
public class TestController {


    @GetMapping("test")
    public String tset() {
        return "hello world";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("user/{id}")
    public Result<String> user(@PathVariable("id") String id) {
        return Result.<String>build().withData("");
    }
}
