package com.john.server.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:21
 * @since jdk1.8
 */
@Data
public class Product {

    @Excel(name = "一级_一级类目编号")
    private String firstCode;

    @Excel(name = "一级_一级类目")
    private String firstName;

    @Excel(name = "二级_二级类目编号")
    private String secondCode;

    @Excel(name = "二级_二级类目")
    private String secondName;


}
