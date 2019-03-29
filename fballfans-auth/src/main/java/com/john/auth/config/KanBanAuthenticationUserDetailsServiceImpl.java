package com.john.auth.config;

import com.john.auth.domain.entity.SysUser;
import com.john.auth.dto.SysUserOutput;
import com.john.auth.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ""
 * @date 2019/2/15
 * @since jdk1.8
 */
@Component
public class KanBanAuthenticationUserDetailsServiceImpl implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        //获取到jwt
        String principal = (String) token.getPrincipal();
        if (StringUtils.isBlank(principal)) {
            throw new RuntimeException("用户不存在");
        }
        SysUserOutput userInfo = JwtTokenUtil.getUserInfo(principal);
        if (userInfo != null) {
            SysUser details = new SysUser();
            BeanUtils.copyProperties(userInfo, details);
            Set<String> authorities = userInfo.getAuthorities();
            Set<GrantedAuthority> authoritySet = authorities.parallelStream()
                    .map(item -> (GrantedAuthority) () -> item)
                    .collect(Collectors.toSet());
            details.setAuthorities(authoritySet);
            return details;
        }
        throw new RuntimeException("用户不存在");
    }
}
