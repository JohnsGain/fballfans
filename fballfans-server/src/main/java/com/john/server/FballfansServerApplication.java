package com.john.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:13
 * @since jdk1.8
 */
@SpringBootApplication()
public class FballfansServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FballfansServerApplication.class, args);
    }

}
