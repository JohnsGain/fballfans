package com.john.auth.service.impl;

import com.john.auth.service.IValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @email 1047994907@qq.com
 * @date 2019/2/23
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@Component
public class SmsCodeProcessorImpl implements IValidateCodeProcessor {

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        String authType = request.getAuthType();
    }

    @Override
    public void validate(ServletWebRequest webRequest) {

    }
}
