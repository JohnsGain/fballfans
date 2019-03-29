package com.john.auth.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author ""
 * @date 2019/2/25
 * @since jdk1.8
 */
public class SysRoleUser implements Serializable {

    private static final long serialVersionUID = -3117054014286250451L;

    private Long id;

    private Long roleId;

    private Long userId;

    private LocalDateTime createTime;

    private String operateIp;

    private String operator;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleUser)) {
            return false;
        }
        SysRoleUser that = (SysRoleUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, roleId, userId);
    }

    @Override
    public String toString() {
        return "SysRoleUser{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", operateIp='" + operateIp + '\'' +
                ", operator='" + operator + '\'' +
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
