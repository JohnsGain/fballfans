package com.example.rabbitmqdemo.domain.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ""
 * @description
 * @date 2018/9/17
 * @since jdk1.8
 */
@Data()
@Entity
@Table(name = "order")
public class Order implements Serializable {
    private static final long serialVersionUID = -2195052925611885720L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private String name;

    @CreationTimestamp
    private LocalDateTime createTime;

}
