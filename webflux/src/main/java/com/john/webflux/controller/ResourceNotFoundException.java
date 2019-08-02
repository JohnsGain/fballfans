package com.john.webflux.controller;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-02 00:19
 * @since jdk1.8
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
