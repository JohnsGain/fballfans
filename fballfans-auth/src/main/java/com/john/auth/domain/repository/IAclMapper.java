package com.john.auth.domain.repository;

import com.john.auth.domain.entity.SysAcl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author ""
 * @date 2019/2/25
 * @since jdk1.8
 */
@Repository
public interface IAclMapper {

    @Select("select a.* from sys_acl a join role_acl ra on ra.acl_id = a.id where ra.role_id = #{roleId}")
    Set<SysAcl> findByRoleId(@Param("roleId") Long roleId);
}
