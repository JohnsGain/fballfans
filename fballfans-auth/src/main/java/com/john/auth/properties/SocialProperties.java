package com.john.auth.properties;

/**
 * spring autoconfig 2.0以上版本配置还没有集成  SocialProperties 这个配置类，所以这里自定义一个
 * @author ""
 * @date 2019/3/11
 * @since jdk1.8
 */
public abstract class SocialProperties {

    private String appId;
    private String appSecret;

    public SocialProperties() {
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
