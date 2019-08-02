package com.john.webflux.controller;

import com.john.webflux.domain.entity.User;
import com.john.webflux.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-01 22:24
 * @since jdk1.8
 */
@RestController
@RequestMapping("test")
public class HelloWorldController {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

    public HelloWorldController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Mono<String> test() {
        LOGGER.info("进入test...");
        return Mono.justOrEmpty("helloWorld");
    }


    @GetMapping("{id}")
    public Mono<User> test2(@PathVariable Integer id) {
        LOGGER.info("进入test2...");
        return userService.getById(id);
    }

    @PostMapping()
    public Mono<User> add(String name, Integer id) {
        LOGGER.info("进入 add...={},{}", name, id);
        User user = new User();
        user.setId(id);
        user.setName(name);
        return userService.createOrUpdate(user);
    }

    @DeleteMapping
    public Mono<User> delete(Integer id) {
        LOGGER.info("进入 delete...");
        return userService.delete(id);
    }

    @GetMapping("list")
    public Flux<User> list() {
        LOGGER.info("进入 list...");
        return userService.list();
    }

}
