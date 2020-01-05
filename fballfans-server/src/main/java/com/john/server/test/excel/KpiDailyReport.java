package com.john.server.test.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 团巴拉销售业绩日报表
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-01 18:38
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDailyReport {

    private Date date;

    private BigDecimal gmv;

    private BigDecimal saleroom;
    private BigDecimal serviceCharge;
    private BigDecimal prevGmv;
    private BigDecimal prevSaleroom;
    private BigDecimal prevServiceCharge;
    private Double gmvChain;
    private Double saleroomChain;
    private Double serviceChargeChain;



}
