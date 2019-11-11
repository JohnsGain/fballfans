package com.john.auth.controller;

import org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping;
import org.springframework.boot.actuate.health.HealthEndpointWebExtension;
import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 acturator包下面的一些类的作用的目的，看看各个 endpoint是怎么加载的
 * 例如 {@link org.springframework.boot.actuate.logging.LogFileWebEndpoint}
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-11 20:46
 * @since jdk1.8
 */
@RestController
@RequestMapping("act")
public class ActuratorController {
//
//    final
//    LogFileWebEndpoint logFileWebEndpoint;
//
//    public ActuratorController(LogFileWebEndpoint logFileWebEndpoint) {
//        this.logFileWebEndpoint = logFileWebEndpoint;
//    }


    @GetMapping("log")
    public void log() {
//        WebMvcEndpointHandlerMapping
//        LogFileWebEndpointAutoConfiguration
//        LogFileWebEndpoint
//        HealthEndpointWebExtension
//        OperationH
//        AbstractWebMvcEndpointHandlerMapping
//        System.out.println(logFileWebEndpoint.logFile());
    }

}
