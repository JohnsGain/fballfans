package com.john.webflux.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-05 07:59
 * @since jdk1.8
 */
@Component
public class CalculatorHandler {

    public Mono<ServerResponse> add(ServerRequest request) {

    }

    private Mono<ServerResponse> calculate(ServerRequest request, BiFunction<Integer, Integer, Integer> function) {

    }

}
