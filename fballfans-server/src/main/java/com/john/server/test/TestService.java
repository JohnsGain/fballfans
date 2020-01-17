package com.john.server.test;

import com.john.server.domain.repository.SysAclRepository;
import com.john.server.domain.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-17 19:16
 * @since jdk1.8
 */
@Service
public class TestService {

    @Autowired
    private SysAclRepository sysAclRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

}
