package com.john.auth.domain.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 权限表
 * @author ""
 * @date 2019/2/21
 * @since jdk1.8
 */
public class SysAcl implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 9190379140299876837L;
    private Long id;

    private String code;

    private String name;

    private Long aclModuleId;

    private String url;

    private Integer type;

    private Integer status;

    private Integer seq;

    private String remark;

    private String operator;

    private LocalDateTime createTime;

    private String operateIp;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysAcl)) {
            return false;
        }
        SysAcl sysAcl = (SysAcl) o;
        return Objects.equals(id, sysAcl.id) &&
                Objects.equals(code, sysAcl.code) &&
                Objects.equals(name, sysAcl.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name);
    }

    public SysAcl() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAclModuleId() {
        return aclModuleId;
    }

    public void setAclModuleId(Long aclModuleId) {
        this.aclModuleId = aclModuleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }
}
