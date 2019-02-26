package com.john.auth.config;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 退出成功处理
 *
 * @author zhangjuwa
 * @date 2019/2/25
 * @since jdk1.8
 */
@Component
public class AjaxLogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public AjaxLogoutSuccessHandlerImpl(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (redisTemplate.hasKey(authorization)) {
            redisTemplate.delete(authorization);
        }
        Result<String> logoutSuccess = Result.<String>build().withCode(HttpStatus.SC_OK).withData("logout success");
        response.getWriter()
                .write(JSON.toJSONString(logoutSuccess));
    }
}
