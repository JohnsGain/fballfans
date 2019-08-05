package com.john.webflux.service;

import com.john.webflux.controller.ResourceNotFoundException;
import com.john.webflux.domain.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-01 23:58
 * @since jdk1.8
 */
@Service
public class UserService {
    private static final Map<Integer, User> DATA = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    static {
        User user = new User();
        user.setId(1);
        user.setName("SDG");
        User user2 = new User();
        user2.setId(2);
        user2.setName("SDG222");
        DATA.put(1, user);
        DATA.put(2, user2);
    }

    public Flux<User> list() {
        return Flux.fromIterable(DATA.values());
    }

    public Flux<User> getById(final Flux<Integer> ids) {
        ids.doOnEach(item -> {
            LOGGER.info("id={}", item);
        });
        //如果流在超时时限没有发出（emit）任何值，则发出错误（error）信号,发生错误的时候就返回 1
        return ids.timeout(Duration.ofSeconds(1)).onErrorResume(item -> Flux.just(1))
                .flatMap(item -> Mono.justOrEmpty(DATA.get(item)));
        //return ids.flatMap(id -> Mono.justOrEmpty(DATA.get(id)));
    }

    public Mono<User> getById(final Integer id) {
        LOGGER.info("getbyteiD={}", id);
        return Mono.justOrEmpty(DATA.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }

    public Mono<User> createOrUpdate(final User user) {
        DATA.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> delete(final Integer id) {
        return Mono.justOrEmpty(DATA.remove(id));
    }
}
