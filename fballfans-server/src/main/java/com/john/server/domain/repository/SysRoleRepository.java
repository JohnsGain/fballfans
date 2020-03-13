package com.john.server.domain.repository;

import com.john.server.domain.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:16
 * @since jdk1.8
 */
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<SysRole> findByOperator(String operator);

    List<SysRole> findByIdBetween(Long gte,Long lte);

    @Query(value = "SELECT * FROM sys_role WHERE operator='1' for update ", nativeQuery = true)
    List<SysRole> findByOperatorForUpdate(String operator);

}
