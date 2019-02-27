package com.john.auth.config;

import com.john.auth.domain.entity.SysUser;
import com.john.auth.dto.SysUserOutput;
import com.john.auth.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
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
            UserDetails details = new SysUser();
            BeanUtils.copyProperties(userInfo, details);
            return details;
        }
        //Object ob = redisTemplate.opsForValue().get(principal);
        //Authentication authentication;
        //if (ob != null) {
        //    authentication = (Authentication) ob;
        //    SysUser principal1 = (SysUser) authentication.getPrincipal();
        //    if (principal1 != null) {
        //        return principal1;
        //    }
        //}
        //if (!StringUtils.isEmpty(principal)) {
        //    //return new KanBanUserDetails(new KanBanUser(principal));
        //    return new User("username", "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //}
        throw new RuntimeException("用户不存在");
    }
}
