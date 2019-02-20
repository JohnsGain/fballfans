package com.john.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/16
 * @Since jdk1.8
 */
@Configuration
@ConfigurationProperties(prefix = "imooc.security")
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties validateCode = new ValidateCodeProperties();

    private OAuth2Properties oauth2 = new OAuth2Properties();

    private SocialParamProperties social = new SocialParamProperties();

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }

    public SocialParamProperties getSocial() {
        return social;
    }

    public void setSocial(SocialParamProperties social) {
        this.social = social;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(ValidateCodeProperties validateCode) {
        this.validateCode = validateCode;
    }
}
