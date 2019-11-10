package com.john;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 如果一个用于json格式化的数据本身就已经是json格式的字符串了，那么json格式化的时候就可以让这份值保持原值，不参与格式化
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-11 00:02
 * @since jdk1.8
 */

public class JsonData {

    @JsonRawValue
    @JsonValue
    private String data;

    private JsonData(String data) {
        this.data = data;
    }

    public static JsonData of(String data) {
        return new JsonData(data);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
