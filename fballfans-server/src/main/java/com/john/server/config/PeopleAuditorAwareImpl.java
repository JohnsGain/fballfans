package com.john.server.config;

import com.john.server.domain.entity.Order;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-11 15:50
 * @since jdk1.8
 */
public class PeopleAuditorAwareImpl implements AuditorAware<Order> {

    @Override
    public Optional<Order> getCurrentAuditor() {
        return Optional.empty();
    }
}
