package com.john.auth.service;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @email 1047994907@qq.com
 * @date 2019/2/22
 * @auther zhangjuwa
 * @since jdk1.8
 **/
public interface IValidateCodeProcessor {

    /**
     * 验证码获取,包括验证码生成，保存，发送
     * @param request
     */
    void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException;
    /**
     * 验证码验证
     * @param webRequest
     */
    void validate(ServletWebRequest webRequest);
}
