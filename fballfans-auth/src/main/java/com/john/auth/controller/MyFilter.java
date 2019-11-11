package com.john.auth.controller;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-11 23:25
 * @since jdk1.8
 */
@WebFilter(urlPatterns = "/*")
@Component
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest,servletResponse);
    }


}
