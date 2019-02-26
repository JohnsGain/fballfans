package com.john.auth.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhangjuwa
 * @date 2019/2/26
 * @since jdk1.8
 */
public class JwtTokenOutput {

    @ApiModelProperty("访问用token")
    private String accessToken;
    @ApiModelProperty("刷新用token")
    private String refreshToken;
    @ApiModelProperty("token类型")
    private String tokenType;
    @ApiModelProperty("过期时间")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
