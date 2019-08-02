package com.john.webflux.service;

import com.john.webflux.controller.ResourceNotFoundException;
import com.john.webflux.domain.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private static final Map<Integer, User> data = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    static {
        User user = new User();
        user.setId(1);
        user.setName("SDG");
        User user2 = new User();
        user2.setId(2);
        user2.setName("SDG222");
        data.put(1, user);
        data.put(2, user2);
    }

    public Flux<User> list() {
        return Flux.fromIterable(this.data.values());
    }

    public Flux<User> getById(final Flux<Integer> ids) {
        ids.doOnEach(item -> {
            LOGGER.info("id={}", item);
        });
        return ids.flatMap(id -> Mono.justOrEmpty(this.data.get(id)));
    }

    public Mono<User> getById(final Integer id) {
        LOGGER.info("getbyteiD={}",id);
        return Mono.justOrEmpty(this.data.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }

    public Mono<User> createOrUpdate(final User user) {
        this.data.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> delete(final Integer id) {
        return Mono.justOrEmpty(this.data.remove(id));
    }
}
