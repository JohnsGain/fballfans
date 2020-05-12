package com.example.rabbitmqdemo.config;

import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-31 16:34
 * @since jdk1.8
 */
@Data
public class UserInfo {

    String username;

    Integer age;

    Long interval;

    Boolean deletee;
}
