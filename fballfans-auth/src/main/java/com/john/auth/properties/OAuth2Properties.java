package com.john.auth.properties;

/**
 * @description
 * @email 1047994907@qq.com
 * @date 2018/12/17
 * @auther zhangjuwa
 * @since jdk1.8
 **/
public class OAuth2Properties {

    private String storeType;

    private String signKey;

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }
}
