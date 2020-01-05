package com.john.server.test.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-01 17:35
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    private String name;

    private String sex;

    private Integer age;

    private String address;
    private String hobby;

}
