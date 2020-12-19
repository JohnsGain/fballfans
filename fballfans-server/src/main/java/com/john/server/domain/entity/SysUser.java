package com.john.server.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author ""
 * @date 2019/2/21
 * @since jdk1.8
 */
@Data()
@Entity
@Table(name = "sys_user")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String telephone;

    private String mail;

    private String password;

    private Long deptId;

    private Integer status;

    private String remark;

    private String operator;

    private LocalDateTime createTime;
}
