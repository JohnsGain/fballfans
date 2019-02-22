package com.john.auth.domain.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/2/21
 * @since jdk1.8
 */
public class SysRole implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 5082650975597098383L;

    private List<SysAcl> sysAcls;

    @Override
    public String getAuthority() {
        return null;
    }

    public List<SysAcl> getSysAcls() {
        return sysAcls;
    }

    public void setSysAcls(List<SysAcl> sysAcls) {
        this.sysAcls = sysAcls;
    }
}
