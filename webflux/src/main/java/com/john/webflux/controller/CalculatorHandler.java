package com.john.webflux.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.BiFunction;

/**
 * webflux中处理具体请求计算逻辑的处理器，在routerHandler里面通过 HandlerFunction 来调用这些方法
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-05 07:59
 * @since jdk1.8
 */
@Component
public class CalculatorHandler {

    public Mono<ServerResponse> add(ServerRequest request) {
        return calculate(request, (a, b) -> a.doubleValue() + b.doubleValue());
    }

    public Mono<ServerResponse> subtract(ServerRequest request) {
        return calculate(request, (a, b) -> a.doubleValue() - b.doubleValue());
    }

    public Mono<ServerResponse> multiple(ServerRequest request) {
        return calculate(request, (a, b) -> a.doubleValue() * b.doubleValue());
    }

    public Mono<ServerResponse> divide(ServerRequest request) {
        return calculate(request, (a, b) -> a.doubleValue() / b.doubleValue());
    }

    private Mono<ServerResponse> calculate(ServerRequest request, BiFunction<Number, Number, Double> function) {
        Tuple2<Integer, Integer> tuple2 = Tuples.of(parseOperant(request, "v1"), parseOperant(request, "v2"));
        Mono<ServerResponse> body = ServerResponse.ok()
                .body(Mono.just(function.apply(tuple2.getT1(), tuple2.getT2())), Double.class);
        return body;
    }

    private int parseOperant(ServerRequest request, String param) {
        String value = request.queryParam(param).orElse("0");
        return Integer.parseInt(value);
    }

}
