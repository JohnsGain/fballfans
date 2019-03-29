package com.john.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.john.auth.CommonConst;
import com.john.auth.domain.entity.SysUser;
import com.john.auth.dto.JwtTokenOutput;
import com.john.auth.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ""
 * @date 2019/2/16
 * @since jdk1.8
 */
@Component
public class MyAuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public MyAuthenticationSuccessHandlerImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken tokens = (UsernamePasswordAuthenticationToken) authentication;
        tokens.eraseCredentials();
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        long jti = System.nanoTime();
        Map<String, Object> map = new HashMap<>(8);
        map.put("username", sysUser.getUsername());
        map.put("nickname", sysUser.getNickname());
        map.put("id", sysUser.getId());
        map.put("remark", sysUser.getRemark());
        map.put("telephone", sysUser.getTelephone());
        map.put("jti", jti);
        Set<String> collect = sysUser.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        //JSONArray objects = JSONArray.parseArray(JSON.toJSONString(collect));
        map.put("authorities", collect);
        String jwt = JwtTokenUtil.generateToken(sysUser.getUsername(), 600, map);
        JwtTokenOutput tokenOutput = new JwtTokenOutput();
        tokenOutput.setAccessToken(jwt);
        tokenOutput.setTokenType("Bearer");
        tokenOutput.setExpiresIn(600 * 1000L);
        map.clear();
        map.put("scope", "refresh");
        tokenOutput.setRefreshToken(JwtTokenUtil.generateToken(sysUser.getUsername(), 3600 * 24 * 7, map));

        redisTemplate.opsForSet().add(CommonConst.KEY_PREFIX + sysUser.getUsername(), jti);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(tokenOutput));
    }
}
