package com.john.auth.properties;

/**
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/10/7
 * @Since jdk1.8
 */
public class QQProperties extends SocialProperties {

    /**
     * 服务提供商唯一标识ID,
     */
    private String providerId = "qq";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
