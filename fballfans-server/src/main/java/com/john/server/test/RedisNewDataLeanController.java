package com.john.server.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-16 15:18
 * @since jdk1.8
 */
@Slf4j
@RestController
@RequestMapping("redis")
public class RedisNewDataLeanController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("ready")
    public void ready() {
        redisService.ready();

    }

    /**
     * @return geo spartial数据结构，可以查询某个点的经纬度，查询两个点之间的距离，查询某个点某个半径范围的数据
     */
    @GetMapping("geo")
    public String geo() {
        GeoOperations<String, Serializable> geo = redisTemplate.opsForGeo();
        return "";
    }

    /**
     * hyperloglog数据类型使用
     */
    @GetMapping("hyperloglog")
    public void hyperloglog() {
        redisService.hyperloglog();
    }

    @GetMapping("scan")
    public Object scan() {
        Set<Object> hashSet = Sets.newConcurrentHashSet();
        redisTemplate.execute((RedisConnection connection) -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(5).match("*").build());
            cursor.forEachRemaining(item -> {
                hashSet.add(new String(item, Charset.defaultCharset()));
            });
            return 0;
        });

        return hashSet;
    }

    /**
     * pipeline命令可以把多个客户端命令放入一个TCP包里面，减少多次发送数据来回传输的RTT时间。
     * 这个方法对于那些没有互相依赖的多个命令执行，能减少多次发送的RTT时间，和redis服务端资源浪费
     *
     * @return
     */
    @GetMapping("pipeline")
    public String pipeline() {
        redisTemplate.opsForValue().set("name", "tom");
//        redisTemplate.opsForSet().

        List<Object> objects = redisTemplate.executePipelined((RedisConnection con) -> {
            Charset utf8 = StandardCharsets.UTF_8;
            con.set("name".getBytes(utf8), "john".getBytes(utf8));
            con.sCard("diffkey".getBytes(utf8));
            con.keyCommands().ttl("k113".getBytes(utf8));
            con.zCount("zkey".getBytes(utf8), 91, 100);
            con.listCommands().lRange("lusers".getBytes(utf8), 0, 5);
            con.hashCommands().hGetAll("musers".getBytes(utf8));
            //            这里必须返回空，可以进去看源码，不为空会报错
            return null;
        });
        log.info("返回结果={}", objects.size());
        objects.forEach(item -> {
            try {
                log.info(item.getClass() + objectMapper.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                log.warn("解析", e);
            }
        });

        return "pipeline";
    }




}
