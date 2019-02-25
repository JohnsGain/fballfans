package com.john.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.john.Result;
import com.john.auth.CommonConst;
import com.john.auth.dto.CaptchaParamOutput;
import com.john.auth.service.IValidateCodeProcessor;
import com.john.auth.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @email 1047994907@qq.com
 * @date 2019/2/23
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@Component
public class ImageCodeProcessorImpl implements IValidateCodeProcessor {



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        if (width == null) {
            width = "110";
        }
        if (height == null) {
            height = "35";
        }
        String code = VerifyCodeUtils.generateVerifyCode(4);
        String base64img = VerifyCodeUtils.base64Image(Integer.valueOf(width), Integer.valueOf(height), code);
        String signature = passwordEncoder.encode(CommonConst.CODE_HEADER + code.toUpperCase());
        CaptchaParamOutput data = new CaptchaParamOutput(base64img, signature);
    }

    @Override
    public void validate(ServletWebRequest webRequest) throws IOException {
        HttpServletRequest request = webRequest.getRequest();
        String contentType = request.getContentType();
        CaptchaParamOutput param = JSON.parseObject(contentType, CaptchaParamOutput.class);
        if (!passwordEncoder.matches(CommonConst.CODE_HEADER + param.getCaptcha().toUpperCase(), param.getSignature())) {
            Result<String> message = Result.<String>build()
                    .withCode(401)
                    .withMessage("签名验证错误");
            HttpServletResponse response = webRequest.getResponse();
            response.getWriter()
                    .write(JSON.toJSONString(message));
        }
    }
}
