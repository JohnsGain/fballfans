package com.john.auth.config;

import com.john.auth.domain.entity.SysRole;
import com.john.auth.domain.entity.SysUser;
import com.john.auth.domain.repository.IAclMapper;
import com.john.auth.domain.repository.IRoleMapper;
import com.john.auth.domain.repository.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author ""
 * @date 2019/2/15
 * @since jdk1.8
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserMapper userMapper;

    private final IRoleMapper roleMapper;

    @Autowired
    private IAclMapper aclMapper;

    @Autowired
    public UserDetailsServiceImpl(IUserMapper userMapper, IRoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser byUsername = userMapper.findByUsername(username);
        if (byUsername == null) {
            throw new BadCredentialsException("用户不存在");
        }
        Set<SysRole> roles = roleMapper.findByUserId(byUsername.getId());
        for (SysRole role : roles) {
            role.setSysAcls(aclMapper.findByRoleId(role.getId()));
        }
        byUsername.setSysRoles(roles);

        //return new User(byUsername.getUsername(), byUsername.getPassword(),
        //        AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return byUsername;
    }

}
