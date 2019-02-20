package com.john.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.auth.properties.BrowserProperties;
import com.john.auth.properties.LoginTypeEnum;
import com.john.auth.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 登录成功处理器
 *
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/16
 * @Since jdk1.8
 */
@Component
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @param request
     * @param response
     * @param authentication 登录成功之后返回的信息，用户信息也包含在里面
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //SecurityContextPersistenceFilter
//        UsernamePasswordAuthenticationFilter
//        SecurityContextPersistenceFilter
        BrowserProperties browser = securityProperties.getBrowser();
        log.info("login success");
        if (LoginTypeEnum.JOSN.equals(browser.getLoginType())) {
            response.setContentType("application/json;charset=utf-8");
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authentication", authentication);
            jsonObject.put("token", token);

            response.getWriter().write(jsonObject.toJSONString());
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
