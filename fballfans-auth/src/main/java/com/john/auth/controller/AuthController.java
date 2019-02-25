package com.john.auth.controller;

import com.john.Result;
import com.john.ResultStatusEnum;
import com.john.auth.dto.CaptchaParamOutput;
import com.john.auth.properties.ValidateCodeTypeEnum;
import com.john.auth.service.IValidateCodeProcessor;
import com.john.auth.service.ValidateCodeProcessorHolder;
import com.john.auth.utils.VerifyCodeUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description
 * @email 1047994907@qq.com
 * @date 2019/2/14
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@RestController
@RequestMapping("auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 验证码header
     */
    private final String CODE_HEADER = "ae81cac2";

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/needlogin")
    public Result unAuthorised(HttpServletRequest request) {
        return Result.build().error(ResultStatusEnum.AUTHENTICATE_FAILED);
    }

    @ApiOperation(value = "获取图片验证码")
    @GetMapping("piccaptcha")
    public Result<CaptchaParamOutput> captcha(@ApiParam("宽度") @RequestParam(defaultValue = "110") int width, @ApiParam("高度") @RequestParam(defaultValue = "34") int height) throws IOException {
        String code = VerifyCodeUtils.generateVerifyCode(4);
        String base64img = VerifyCodeUtils.base64Image(width, height, code);
        String signature = passwordEncoder.encode(CODE_HEADER + code.toUpperCase());
        CaptchaParamOutput data = new CaptchaParamOutput(base64img, signature);
        return Result.<CaptchaParamOutput>build()
                .ok()
                .withData(data);
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("verificationcode")
    public void getCode(@ApiParam("验证吗类型") @RequestParam("codeType") ValidateCodeTypeEnum codeType,
                        HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        IValidateCodeProcessor codeProcessor = validateCodeProcessorHolder.findCodeProcessor(codeType);
        codeProcessor.create(request, response);
    }
}
