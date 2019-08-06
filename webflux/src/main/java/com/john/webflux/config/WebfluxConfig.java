package com.john.webflux.config;

import com.john.webflux.controller.CalculatorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-02 08:21
 * @since jdk1.8
 */
@Configuration
public class WebfluxConfig {

    /**
     * 在创建了处理请求的 HandlerFunction 之后，下一步是为这些 HandlerFunction 提供路由信息，也就是这些 HandlerFunction
     * 被调用的条件。这是通过函数式接口
     * @param handler
     * @return
     */
    @Bean
    @Autowired
    public RouterFunction<ServerResponse> routerFunction(CalculatorHandler handler) {
        RouterFunctions.Builder calculate = RouterFunctions.route()
                .GET("/calculate/add", item -> handler.add(item))
                .GET("/calculate/subtract", item -> handler.subtract(item))
                .GET("/calculate/multiple", item -> handler.multiple(item))
                .GET("/calculate/divide", item -> handler.divide(item));
        RouterFunction<ServerResponse> build = calculate.build();


        return build;
    }
}
