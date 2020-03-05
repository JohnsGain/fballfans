package com.john.server.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-21 11:48
 * @since jdk1.8
 */
@Configuration
@PropertySource(value = {"classpath:demo"})
@ComponentScan(basePackages = "com.john.server.test")
public class ConfigProperties {

}
