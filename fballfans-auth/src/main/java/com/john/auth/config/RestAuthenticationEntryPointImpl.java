package com.john.auth.config;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在 KanBanPreAuthenticationFilter 过滤器认证失败之后，springsecurity走到最后一个过滤连上面的最后一个过滤器ExceptionTranslationFilter
 * 之后，某种异常清行，比如{@link KanBanAuthenticationUserDetailsServiceImpl#loadUserDetails(PreAuthenticatedAuthenticationToken)}
 * 抛出的异常，会执行 AuthenticationEntryPoint 接口的是实现，默认实现{@link LoginUrlAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
 * 会重定向到登录路径，z这里配置自定义的 AuthenticationEntryPoint 实现，简单返回401给前端
 * {@link ExceptionTranslationFilter#sendStartAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, AuthenticationException)}
 *
 * @author zhangjuwa
 * @date 2019/2/15
 * @since jdk1.8
 */
@Component
public class RestAuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Result<String> stringResult = Result.<String>build().withCode(HttpStatus.SC_UNAUTHORIZED).withData("未授权");
        response.getWriter().write(JSON.toJSONString(stringResult));
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
