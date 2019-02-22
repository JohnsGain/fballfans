package com.john.auth.config;

import com.john.auth.domain.entity.SysUser;
import com.john.auth.domain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @date 2019/2/15
 * @since jdk1.8
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            throw new BadCredentialsException("用户不存在");
        }
        return new User(byUsername.getUsername(), byUsername.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
