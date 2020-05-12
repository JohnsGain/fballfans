package com.example.rabbitmqdemo.domain.repository;

import com.example.rabbitmqdemo.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ""
 * @description
 * @date 2018/9/17
 * @since jdk1.8
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
