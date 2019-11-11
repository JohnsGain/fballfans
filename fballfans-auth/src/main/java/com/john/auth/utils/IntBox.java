package com.john.auth.utils;

import lombok.Data;

/**
 * 避免 对 包装类进行频算术运算造成的大量  拆装箱，频繁创建对象
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 00:41
 * @since jdk1.8
 */
@Data
public class IntBox {

    private int data;

}
