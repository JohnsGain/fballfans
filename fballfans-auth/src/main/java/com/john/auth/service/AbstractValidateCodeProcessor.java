package com.john.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.ServletRequestBindingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangjuwa
 * @date 2019/2/23
 * @since jdk1.8
 */
public abstract class AbstractValidateCodeProcessor implements IValidateCodeProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {

    }


    protected void save(String code) {

    }

    protected abstract void send(String code);
}
