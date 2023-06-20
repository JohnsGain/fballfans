package com.john.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2021/4/18 00:13
 * @since jdk1.8
 */
@RestController
@Slf4j
public class DeferedResultController {

    private static final Map<String, DeferredResult<String>> map = new ConcurrentHashMap<>();

    @PostMapping("post")
    public DeferredResult<String> post(String name) {
        DeferredResult<String> result = new DeferredResult<>(10000L, () -> "超时了");
        map.put(name, result);
        result.onTimeout(() -> {
            map.remove(name);
            System.out.println("超时函数执行");
            //            log.info("超时函数执行");
        });

        return result;
    }

    @PostMapping("get")
    public String get(String name, String data) {
        DeferredResult<String> result = map.get(name);
        if (result == null) {
            result.setErrorResult("空指针");
            throw new RuntimeException("不能为空");
        }
        result.setResult(data);
        return "SUCCESS";
    }

}
