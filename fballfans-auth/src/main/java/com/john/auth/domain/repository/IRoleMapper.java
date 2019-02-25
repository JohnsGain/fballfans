package com.john.auth.domain.repository;

import com.john.auth.domain.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author zhangjuwa
 * @date 2019/2/25
 * @since jdk1.8
 */
@Repository
public interface IRoleMapper {

    //@Results({@Result(property = "sysAcls", column = "id", javaType = Set.class,
    //        many = @Many(select = "com.john.auth.domain.repository.IAclMapper.findByRoleId"))})
    @Select("select r.*, r.name name from sys_role r join user_role ur on ur.role_id = r.id where ur.user_id = #{userId}")
    Set<SysRole> findByUserId(@Param("userId") Long userId);
}
