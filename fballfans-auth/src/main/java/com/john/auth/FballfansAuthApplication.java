package com.john.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

/**
 * @email 1047994907@qq.com
 * @date 2019/2/13
 * @author zhangjuwa
 * @since jdk1.8
 **/
@SpringBootApplication
@MapperScan(basePackages = {"com.john.auth.domain.repository"}, annotationClass = Repository.class)
public class FballfansAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FballfansAuthApplication.class, args);
    }
}
