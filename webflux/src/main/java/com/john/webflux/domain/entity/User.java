package com.john.webflux.domain.entity;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-01 23:59
 * @since jdk1.8
 */
public class User {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
