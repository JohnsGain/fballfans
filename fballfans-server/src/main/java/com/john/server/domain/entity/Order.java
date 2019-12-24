package com.john.server.domain.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Data()
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long memberId;
    private long metaOrderId;
    private String flowId;
    private long paymentId;

    private String name;
    private String state;
    private String ruleResults;
    private String stateHistory;
    private String memo;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    private Date payTime;
    private Date deliverTime;
    private Date successTime;
    private Date refundTime;
    // 产品标价
    private BigDecimal price;
    // 下面所有{@see OrderItem#amount()}的和, 其中不包含运费
    private BigDecimal amount;
    private long providerId;
    private Integer rate;
    private String providerName;
    private String payInfo;
    private String remark;
    private int soCount;
    private int integral;
    private String orderRemarkState;
    private String oType;
    private String productsJson;
    // Transient , 在使用时无法保证一定存在

    @Transient
    HashMap<String, Object> remarks;
    private BigDecimal shipFee = BigDecimal.ZERO;
    private long leaderId;
    private long assistId;
    private String clientData;
    /**
     * 删除状态 0：否 1：是
     */
    private int deleted;
    // 退款原因
    private String refundReason;
    // 当前系统时间
    @Transient
    private String currentTime;
    @Transient
    private String createTime;


}
