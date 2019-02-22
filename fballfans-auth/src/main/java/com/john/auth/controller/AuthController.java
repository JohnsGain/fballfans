package com.john.auth.controller;

import com.john.Result;
import com.john.ResultStatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/security/needlogin")
    public Result unAuthorised(HttpServletRequest request) {
        return Result.build().error(ResultStatusEnum.AUTHENTICATE_FAILED);
    }
}
