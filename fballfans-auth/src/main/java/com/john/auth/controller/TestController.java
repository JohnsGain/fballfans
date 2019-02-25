package com.john.auth.controller;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import com.john.auth.domain.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @date 2019/2/13
 * @author  zhangjuwa
 * @since jdk1.8
 **/
@RestController
@RefreshScope
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Value("${name}")
    private String applicationName;

    @GetMapping("test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String tset(HttpServletRequest request) throws IOException {
        String authType = request.getAuthType();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String content = null;
        while ((content = bufferedReader.readLine()) != null) {
            builder.append(content);
        }

        LOGGER.info("body= {}", builder);
        SysUser sysUser = JSON.parseObject(builder.toString(), SysUser.class);
        //return "hhhhhh";
        return applicationName + authType +sysUser;
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
