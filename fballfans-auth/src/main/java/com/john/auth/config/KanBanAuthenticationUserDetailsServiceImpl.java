package com.john.auth.config;

import com.john.auth.domain.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String principal = (String) token.getPrincipal();
        Object ob = redisTemplate.opsForValue().get(principal);
        Authentication authentication;
        if (ob != null) {
            authentication = (Authentication) ob;
            SysUser principal1 = (SysUser) authentication.getPrincipal();
            if (principal1 != null) {
                return principal1;
            }
        }
        //if (!StringUtils.isEmpty(principal)) {
        //    //return new KanBanUserDetails(new KanBanUser(principal));
        //    return new User("username", "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //}
        throw new BadCredentialsException("用户不存在");
    }
}
