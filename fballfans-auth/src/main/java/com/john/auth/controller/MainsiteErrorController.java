package com.john.auth.controller;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ""
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
    public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object attribute = request.getAttribute("javax.servlet.error.exception");
        //获取statusCode:401,404,500
        if (attribute instanceof BadCredentialsException) {
            BadCredentialsException e = (BadCredentialsException) attribute;
            response.setContentType("application/json;charset=utf-8");
            Result<String> stringResult = Result.<String>build().withCode(HttpStatus.UNAUTHORIZED.value()).withData(e.getMessage());
            return JSON.toJSONString(stringResult);
        }
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
