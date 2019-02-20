package com.john.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.SimpleResponse;
import com.john.auth.properties.BrowserProperties;
import com.john.auth.properties.LoginTypeEnum;
import com.john.auth.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/16
 * @Since jdk1.8
 */
@Component
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    private final Logger log = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("login failure");
        BrowserProperties browser = securityProperties.getBrowser();
        if (LoginTypeEnum.JOSN.equals(browser.getLoginType())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(e.getMessage())));
        } else {
            super.onAuthenticationFailure(request, response, e);
        }
    }
}
