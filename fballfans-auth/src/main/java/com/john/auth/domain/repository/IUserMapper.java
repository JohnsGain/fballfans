package com.john.auth.domain.repository;

import com.john.auth.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author ""
 * @date 2019/2/15
 * @since jdk1.8
 */
@Repository
public interface IUserMapper {

    @Select("select * from sys_user where username = #{username}")
    SysUser findByUsername(@Param("username") String username);
}
