package com.john.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hero
 * @date 7/12/2017
 */
@ConfigurationProperties(prefix = "jwt.audience")
@Configuration
public class JwtAudience {
    /**
     * 授权者
     */
    private String iss;
    /**
     * 鉴权者
     */
    private String aud;
    /**
     * Base64编码后的密钥
     */
    private String base64Secret;
    /**
     * token 有效时间: 单位s
     */
    private int expirationSeconds;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public int getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(int expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
