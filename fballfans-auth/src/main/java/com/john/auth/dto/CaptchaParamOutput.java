package com.john.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author Duhuafei
 * @date 5/26/2018
 */
@ApiModel("验证码")
public class CaptchaParamOutput {

    @ApiModelProperty(value = "验证码", notes = "获取验证码时返回Base64图片String, 校验时上传用户输入的code")
    @NotBlank
    private String captcha;

    @ApiModelProperty("签名")
    @NotBlank
    private String signature;

    public CaptchaParamOutput() {
    }

    public CaptchaParamOutput(String captcha, String signature) {
        this.captcha = captcha;
        this.signature = signature;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
