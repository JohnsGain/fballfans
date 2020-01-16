package com.john.server.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-16 15:18
 * @since jdk1.8
 */
@RestController
public class RedisNewDataLeanController {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @GetMapping("geo")
    public String geo() {
        GeoOperations<String, Serializable> geo = redisTemplate.opsForGeo();
        return "";
    }

}
