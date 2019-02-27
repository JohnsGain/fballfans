package com.john.auth.config;

import com.john.auth.CommonConst;
import com.john.auth.dto.SysUserOutput;
import com.john.auth.utils.JwtTokenUtil;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjuwa
 * @date 2019/2/15
 * @since jdk1.8
 */
public class KanBanPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String SSO_TOKEN = "Authorization";
    private static final String SSO_CREDENTIALS = "N/A";

    private static final Logger LOGGER = LoggerFactory.getLogger(KanBanPreAuthenticationFilter.class);

    private RedisTemplate<String, Serializable> redisTemplate;

    @Value("${auth.permits}")
    private String[] permits;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public KanBanPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String jwt = request.getHeader(SSO_TOKEN);
        if (request instanceof FirewalledRequest) {
            //StrictHttpFirewall firewall = (StrictHttpFirewall) request;
            FirewalledRequest firewalledRequest = (FirewalledRequest) request;
            RequestFacade facade = (RequestFacade) firewalledRequest.getRequest();
            String requestURI = facade.getRequestURI();
            for (String permit : permits) {
                if (pathMatcher.match(permit, requestURI)) {
                    return jwt;
                }
            }
        }

        //判断是否已经退出登录
        SysUserOutput userInfo = JwtTokenUtil.getUserInfo(jwt);
        if (userInfo == null) {
            throw new BadCredentialsException("无效的token");
        }
        Boolean member = redisTemplate.opsForSet().isMember(CommonConst.KEY_PREFIX + userInfo.getUsername(), userInfo.getJti());
        if (!member) {
            throw new BadCredentialsException("无效的token");
        }
        //验证jwt是否过期

        boolean bearer = JwtTokenUtil.isExpiration(jwt.replace("Bearer ", ""));
        if (userInfo.getExp() < System.currentTimeMillis()) {
            Map<String, Object> map = new HashMap<>(8);
            map.put("username", userInfo.getUsername());
            map.put("nickname", userInfo.getNickname());
            map.put("id", userInfo.getId());
            map.put("remark", userInfo.getRemark());
            map.put("telephone", userInfo.getTelephone());
            map.put("jti", userInfo.getJti());
            //刷新jwt
            jwt = JwtTokenUtil.generateToken(userInfo.getUsername(), 600, map);
        }
        return jwt;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return SSO_CREDENTIALS;
    }

    public RedisTemplate<String, Serializable> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //super.unsuccessfulAuthentication(request, response, failed);
        LOGGER.warn(failed.getLocalizedMessage());
    }
}
