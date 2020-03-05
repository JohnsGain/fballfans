package com.john.server.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-21 11:57
 * @since jdk1.8
 */
public class Appmian {

    /**
     * @param args  配置使用{@link org.springframework.context.annotation.PropertySource}注解来加载
     *              .properties文件。
     *              使用@Value{${key:default}}模式，key是spring上下文配置的参数，如果没有获取到这个值，就读取模式值
     */
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigProperties.class);

        IFileService bean = applicationContext.getBean(FileServiceImpl.class);

        bean.readValues();
    }

}
