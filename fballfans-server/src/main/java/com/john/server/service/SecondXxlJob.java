package com.john.server.service;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-05-10 23:35
 * @since jdk1.8
 */
@Component
@Slf4j
public class SecondXxlJob {

    @XxlJob(value = "calculate")
    public ReturnT<String> calculate(String param) {
        log.info("输入参数={}", param);
        return ReturnT.SUCCESS;
    }

    @XxlJob(value = "exportOrder")
    public ReturnT<String> exportOrder(String param) {
        log.info("输入参数={},count={}", param);
        return ReturnT.SUCCESS;
    }

}
