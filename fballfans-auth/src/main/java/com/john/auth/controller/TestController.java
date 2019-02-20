package com.john.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
    public String test() {
        return "hello world!";
    }
}
