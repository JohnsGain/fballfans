package com.john.auth.domain.entity;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户表
 *
 * @author zhangjuwa
 * @date 2019/2/21
 * @since jdk1.8
 */
public class SysUser implements Serializable, UserDetails {

    private static final long serialVersionUID = -1144253450237160559L;

    private Long id;

    private String username;

    private String nickname;

    private String telephone;

    private String mail;

    private String password;

    private Long deptId;

    private Integer status;

    private String remark;

    private String operator;

    private LocalDateTime createTime;

    private Set<SysRole> sysRoles;

    private Collection<? extends GrantedAuthority> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isNotEmpty(this.authorities)) {
            return authorities;
        }
        Set<GrantedAuthority> list = new HashSet<>(16);
        for (SysRole item : sysRoles) {
            list.add(item);
            list.addAll(item.getSysAcls());
        }
        return list;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysUser)) {
            return false;
        }
        SysUser sysUser = (SysUser) o;
        return Objects.equals(id, sysUser.id) &&
                Objects.equals(username, sysUser.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", deptId=" + deptId +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", operator='" + operator + '\'' +
                ", createTime=" + createTime +
                ", sysRoles=" + sysRoles +
                '}';
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Set<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(Set<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }
}
