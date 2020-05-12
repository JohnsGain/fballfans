package com.example.rabbitmqdemo.config;

import org.junit.Test;
import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;

/**
 * https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/properties/PropertyMapper.html
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-10-31 16:30
 * @since jdk1.8
 */
public class PropertyMapperDemo {

    /**
     * 这个类用于 把一个bean属性值也同样付给另外一个bean
     */
    @Test
    public void mappe() {
        User user = new User();
        UserInfo info = new UserInfo();
        PropertyMapper propertyMapper = PropertyMapper.get();
        propertyMapper.from(user.getName()).to(info::setUsername);

        propertyMapper.from(user::getAge).to(info::setAge);

        propertyMapper.from(user::getDuration)
//                .as((Function<Duration, Object>) Duration::toMillis)
                .as(Duration::getSeconds)
                .to(info::setInterval);

        propertyMapper.from(user::getDeleted)
                .whenTrue()
                .to(info::setDeletee);
    }

}
