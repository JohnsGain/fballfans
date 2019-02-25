package com.john.auth.controller;

import com.john.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description
 * @email 1047994907@qq.com
 * @date 2019/2/13
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@RestController
@RefreshScope
public class TestController {

    @Value("${name}")
    private String applicationName;

    @GetMapping("test")
    public String tset(HttpServletRequest request) {
        String authType = request.getAuthType();
        //return "hhhhhh";
        return applicationName + authType;
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
