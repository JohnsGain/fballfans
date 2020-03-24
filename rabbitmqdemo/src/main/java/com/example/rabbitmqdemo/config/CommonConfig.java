package com.example.rabbitmqdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-18 15:39
 * @since jdk1.8
 */
@Configuration
public class CommonConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
//在序列化时 Timestamp 实例日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//在序列化时忽略值为 null 的属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//忽略值为默认值的属性
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        //可以把 LocalDateTime 格式化成 2020-03-18T15:57:59.907,需要配置这个，否则对Duration或者Localdatetime等类型
        //类型反序列化会报错
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        objectMapper.registerModule(new JavaTimeModule());
//        User user = new User();
//        user.setDate(new Date());
//
//        user.setAge(5);
//        user.setDeleted(true);
//        user.setDuration(Duration.ofMillis(12));
//        user.setNow(LocalDateTime.now());
//        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        String value = objectMapper.writerWithDefaultPrettyPrinter()
//                .writeValueAsString(user);
//        System.out.println(value);
//
//        User user1 = objectMapper.readValue(value, User.class);
//        System.out.println(user1);
//
//
//    }

}
