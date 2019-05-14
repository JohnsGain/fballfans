package com.fballfans.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import java.io.Serializable;

/**
 * 配置索引名称是 world, 类型默认是实体类名的小写，比如这里就是 country
 *
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Document(indexName = "world")
public class Country implements Serializable {

    private static final long serialVersionUID = 2725635124304034708L;

    @Id
    @GeneratedValue
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
