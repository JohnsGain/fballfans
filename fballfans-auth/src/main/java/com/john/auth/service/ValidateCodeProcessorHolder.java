package com.john.auth.service;

import com.john.auth.properties.ValidateCodeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/23
 * @Since jdk1.8
 */
@Component
public class ValidateCodeProcessorHolder {

    /**
     * 多个{@link IValidateCodeProcessor}的实现bean都会注入这个Map,以beanId为key
     */
    @Autowired
    private Map<String, IValidateCodeProcessor> validateCodeProcessorMap;

    public IValidateCodeProcessor findCodeProcessor(ValidateCodeTypeEnum typeEnum) {
        return validateCodeProcessorMap.get(typeEnum.name());
//        return null;
    }

}
