package com.john.webflux.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-02 00:18
 * @since jdk1.8
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    private final ObjectMapper objectMapper;

    public MyExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ObjectNode> resourceNotFoundExceptionHander(ResourceNotFoundException e) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("message", e.getMessage());
        objectNode.put("code", 200);
        objectNode.put("data", "");
        log.info("异常={}", e);
        return Mono.justOrEmpty(objectNode);
    }

}
