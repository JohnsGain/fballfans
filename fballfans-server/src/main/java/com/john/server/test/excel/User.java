package com.john.server.test.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-01 17:35
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {


    private static final long serialVersionUID = 5806344070093285553L;
    private String name;

    private String sex;

    private Integer age;

    private String address;
    private String hobby;

}
