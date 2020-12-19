package com.john.server.domain.repository;

import com.john.server.domain.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:16
 * @since jdk1.8
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    @Modifying
    @Query(value = "update sys_user a set a.deptId=a.deptId-:inc where a.id = 2", nativeQuery = true)
    void updateAge(int inc);
}
