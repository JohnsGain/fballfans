package com.john.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;

/**
 * @author ""
 * @email 1047994907@qq.com
 * @date 2019/2/13
 * @since jdk1.8
 **/
@SpringBootApplication
@MapperScan(basePackages = {"com.john.auth.domain.repository"}, annotationClass = Repository.class)
@EnableScheduling
@EnableDiscoveryClient
public class FballfansAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FballfansAuthApplication.class, args);
    }
}
