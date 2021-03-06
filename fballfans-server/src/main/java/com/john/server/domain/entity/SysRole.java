package com.john.server.domain.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ""
 * @date 2019/2/21
 * @since jdk1.8
 */
@Data()
@Entity
@Table(name = "sys_role")
public class SysRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer type;

    private Integer status;

    private String remark;

    private String operator;

    @CreationTimestamp
    private LocalDateTime createTime;


}
