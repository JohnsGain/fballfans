package com.john.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 获取用户详细信息实现，应该由具体的义务系统实现，所以这个类放入demo子项目下面
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/16
 * @Since jdk1.8
 */
@Component
public class MyUserDetailServiceImple implements UserDetailsService, SocialUserDetailsService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailServiceImple.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UsernamePasswordAuthenticationFilter
        return buildUserDetails(username);
    }

    /**
     * 通过第三方社交账号登录之后，根据服务提供商ID和用户在服务提供山那里的唯一ID即openId在
     * 数据表userconnection里面找到的用户在当前系统中的唯一用户ID,
     * 然后用这个用户ID来进行登录获取用户在该义务系统的信息
     * @param userId  义务系统里面的用户唯一标识，主键用户ID或者身份证号，手机号等
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        LOGGER.info("登录用户ID{}", userId);
        return buildUserDetails(userId);
    }

    private SocialUserDetails buildUserDetails(String userId) {
        String encode = passwordEncoder.encode("123456");
        return new SocialUser(userId, encode, true,true, true, true,
                //角色ROLE_USER当前服务作为服务提供商的时候，必须要用户具有的角色，
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
    }
}
