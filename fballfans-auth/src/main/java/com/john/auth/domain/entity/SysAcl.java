package com.john.auth.domain.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * 权限表
 * @author zhangjuwa
 * @date 2019/2/21
 * @since jdk1.8
 */
public class SysAcl implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 9190379140299876837L;

    @Override
    public String getAuthority() {
        return null;
    }
}
