package com.john.server.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.server.test.excel.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-21 15:47
 * @since jdk1.8
 */
@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public void ready() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setAddress("address" + i);
            user.setAge(5 + i);
            user.setSex((1 ^ i) + "");
            user.setName("john" + i);
            list.add(user);
        }
        redisTemplate.opsForList().leftPushAll("lusers", (Collection) list);
        Map<String, User> collect = list.stream().collect(Collectors.toMap(User::getName, item -> item));

        redisTemplate.opsForHash().putAll("musers", collect);

        Map<Serializable, Point> keyValue = new HashMap<>(16);
        Random random = new Random();
        GeoOperations<String, Serializable> geoOperations = redisTemplate.opsForGeo();
        for (int i = 0; i < 200; i++) {
            double nextDouble = random.nextDouble();
            RedisGeoCommands.GeoLocation<Serializable> location = new RedisGeoCommands.GeoLocation<>("gz" + i,
                    new Point(100 + nextDouble, 22 + nextDouble));
            geoOperations.add("gzgeo", location);
        }
        List<Point> position = redisTemplate.opsForGeo().position("gzgeo", "gz1", "gz2");
        try {
            log.info("经纬度={}", objectMapper.writeValueAsString(position));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//            redisTemplate.opsF
    }
}
