package com.john.auth.config;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangjuwa
 * @date 2019/2/16
 * @since jdk1.8
 */
@Component
public class MyAuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        //Result<String> message = Result.<String>build().withCode(HttpStatus.UNAUTHORIZED.value()).withMessage("用户名或密码错误");
        Result<String> stringResult = Result.<String>build().withCode(HttpStatus.UNAUTHORIZED.value()).withData(e.getLocalizedMessage());
        response.getWriter().write(JSON.toJSONString(stringResult));
    }
}
