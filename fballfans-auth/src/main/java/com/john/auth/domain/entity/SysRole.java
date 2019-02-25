package com.john.auth.domain.entity;

import com.john.auth.CommonConst;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhangjuwa
 * @date 2019/2/21
 * @since jdk1.8
 */
public class SysRole implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 5082650975597098383L;
    private Long id;

    private String name;

    private Integer type;

    private Integer status;

    private String remark;

    private String operator;

    private LocalDateTime createTime;

    private Set<SysAcl> sysAcls;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRole)) {
            return false;
        }
        SysRole sysRole = (SysRole) o;
        return Objects.equals(id, sysRole.id) &&
                Objects.equals(name, sysRole.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * 设置角色名 如果不是以ROLE_开始，在前面加上ROLE_
     *
     * @param name
     */
    public void setName(String name) {
        if (name != null) {
            if (!name.startsWith(CommonConst.ROLE_)) {
                this.name = CommonConst.ROLE_ + name;
            } else {
                this.name = name;
            }
        }
        //this.name = RoleNameUtil.nameStandard(name);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Set<SysAcl> getSysAcls() {
        return sysAcls;
    }

    public void setSysAcls(Set<SysAcl> sysAcls) {
        this.sysAcls = sysAcls;
    }
}
