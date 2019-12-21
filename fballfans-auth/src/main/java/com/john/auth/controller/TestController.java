package com.john.auth.controller;

import com.alibaba.fastjson.JSON;
import com.john.JsonData;
import com.john.Result;
import com.john.auth.domain.entity.SysUser;
import com.john.auth.service.RedisDistributedLockService;
import com.john.auth.service.impl.TaskSchedulerService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import test.UserTest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author ""
 * @date 2019/2/13
 * @since jdk1.8
 **/
@RestController
@RefreshScope
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Value("${name}")
    private String applicationName;

    private final RedisDistributedLockService redisDistributedLockService;

    private final TaskSchedulerService taskSchedulerService;

//    @Value("${spring.redis.cluster.nodes}")
    private String[] nodes;

    @Autowired
    public TestController(RedisDistributedLockService redisDistributedLockService, TaskSchedulerService taskSchedulerService) {
        this.redisDistributedLockService = redisDistributedLockService;
        this.taskSchedulerService = taskSchedulerService;
    }

    @GetMapping("test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String tset(HttpServletRequest request) throws IOException {
        String authType = request.getAuthType();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(),StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String content = null;
        while ((content = bufferedReader.readLine()) != null) {
            builder.append(content);
        }

        LOGGER.info("body= {}", builder);
        SysUser sysUser = JSON.parseObject(builder.toString(), SysUser.class);
        //return "hhhhhh";
        return applicationName + authType + sysUser;
    }

    public static final String KEY = "key";

    @GetMapping("hello")
    public String hello() {
        //Redis集群组态的最低要求是必须有三个主节点
        Config config = new Config();
        config.setLockWatchdogTimeout(50000L);
        ClusterServersConfig clusterServers = config.useClusterServers();
        String[] redisNodes = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            redisNodes[i] = "redis://" + nodes[i];
        }
        clusterServers.addNodeAddress(redisNodes).setReadMode(ReadMode.MASTER_SLAVE)
                .setTimeout(20000);


        RedissonClient redissonClient = Redisson.create(config);
        RLock lock = redissonClient.getLock(KEY);
        try {
            if (lock.tryLock(3000L, TimeUnit.MICROSECONDS)) {
                try {
                    LOGGER.info("sdg12342423");
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {

        }
        return "hello world";
    }

    @GetMapping("user/{id}")
    public Result<String> user(@PathVariable("id") String id) {
        return Result.<String>build().withData("");
    }


    /**
     * 测试手动发起定时任务
     *
     * @throws NoSuchMethodException
     */
    @GetMapping("cron")
    public void cron() throws NoSuchMethodException {
        taskSchedulerService.taskScheduler();
    }

    /**
     * @return 演示JsonRowValue注解  和  JsonValue注解的使用
     */
    @GetMapping("jack")
    public Result jackson() {
        UserTest userTest = new UserTest();
        userTest.setAge(2);
        userTest.setName("{\"country\":\"China\", \"city\":\"Dalian\"}");
        userTest.setName("[{\"type\":\"wx\",\"amount\":3.20},{\"type\":\"leader\",\"amount\":1.80}]");

        String data = "{\"country\":\"China\", \"city\":\"Dalian\"}";
//如果预期的值是一个Json格式的字符串，就不需要再用jackson做序列化了，用一个注解JsonRawValue保证不对这个字符串重复序列化。
        JsonData of = JsonData.of(data);
//        return Result.of(of);
        return Result.build().withData(userTest);
    }



}


