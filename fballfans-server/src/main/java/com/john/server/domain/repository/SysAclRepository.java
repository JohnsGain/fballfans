package com.john.server.domain.repository;

import com.john.server.domain.entity.SysAcl;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:16
 * @since jdk1.8
 */
public interface SysAclRepository extends JpaRepository<SysAcl, Long> {

}
