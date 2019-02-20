package com.john.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @email 1047994907@qq.com
 * @date 2019/2/14
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@RestController
@RequestMapping("auth")
public class AuthController {

    @GetMapping("needlogin")
    public String needLogin() {
        return "needLgoin";
    }
}
