//package com.john.auth.config;
//
//import com.john.auth.properties.SecurityProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 验证码生成器配置
// *
// * @author NANUTO
// * @date 2018年3月10日 下午6:14:28
// */
//@Configuration
//public class ValidateCodeGeneratorConfig {
//
//    @Autowired
//    private SecurityProperties securityProperties;
//
//    @Bean
//    @ConditionalOnMissingBean(name = {"imageCodeGeneratorImpl"})
//    public IValidateCodeGenerator imageCodeGeneratorImpl() {
//        ImageCodeGeneratorImpl generator = new ImageCodeGeneratorImpl();
//        generator.setSecurityProperties(securityProperties);
//        return generator;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(ISmsService.class)
//    public ISmsService defaultSmsServiceImpl() {
//        return new DefaultSmsServiceImpl();
//    }
//
//}
