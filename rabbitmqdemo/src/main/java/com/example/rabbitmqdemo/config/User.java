package com.example.rabbitmqdemo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-30 17:38
 * @since jdk1.8
 */
@Data
//用于类， 指定属性在序列化时 json 中的顺序 ，
// 示例：@JsonPropertyOrder({ "birth_Date", "name" }) public class Person
//@JsonPropertyOrder({"createTime","name","age"})
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;


    private Integer age;

    private Duration duration;

    private Boolean deleted;

    private Timestamp createTime;
    private LocalDateTime now;
    private Date date;


}
