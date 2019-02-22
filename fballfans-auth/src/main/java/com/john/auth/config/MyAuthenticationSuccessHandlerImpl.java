package com.john.auth.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @date 2019/2/16
 * @since jdk1.8
 */
@Component
public class MyAuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        JSONObject objec = new JSONObject();
        objec.put("authentication", authentication);
        objec.put("token", token);
        redisTemplate.opsForValue().set(token, authentication);
        redisTemplate.expire(token, 600, TimeUnit.SECONDS);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objec.toJSONString());
    }
}
