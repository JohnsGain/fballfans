package com.john.auth.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangjuwa
 * @date 2019/2/26
 * @since jdk1.8
 */
@RestController
public class MainsiteErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 401) {
            return "/401";
        } else if (statusCode == 404) {
            return "/404";
        } else if (statusCode == 403) {
            return "/403";
        } else {
            return "/500";
        }

    }

}
