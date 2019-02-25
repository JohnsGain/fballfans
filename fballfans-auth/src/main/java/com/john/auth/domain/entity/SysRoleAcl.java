package com.john.auth.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author zhangjuwa
 * @date 2019/2/25
 * @since jdk1.8
 */
public class SysRoleAcl implements Serializable {

    private static final long serialVersionUID = 6846690908232818332L;

    private Long id;

    private Long roleId;

    private Long aclId;

    private String operator;

    private LocalDateTime createTime;

    private String operateIp;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleAcl)) {
            return false;
        }
        SysRoleAcl that = (SysRoleAcl) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(aclId, that.aclId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, roleId, aclId);
    }

    @Override
    public String toString() {
        return "SysRoleAcl{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", aclId=" + aclId +
                ", operator='" + operator + '\'' +
                ", createTime=" + createTime +
                ", operateIp='" + operateIp + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAclId() {
        return aclId;
    }

    public void setAclId(Long aclId) {
        this.aclId = aclId;
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

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }
}
