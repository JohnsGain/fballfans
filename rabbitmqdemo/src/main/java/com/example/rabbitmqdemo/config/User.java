package com.example.rabbitmqdemo.config;

import lombok.Data;

import java.time.Duration;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 17:38
 * @since jdk1.8
 */
@Data
public class User {

    String name;


    Integer age;

    Duration duration;

    Boolean deleted;

}
