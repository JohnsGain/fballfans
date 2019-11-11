package com.john.auth.utils;

import com.john.Result;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 00:30
 * @since jdk1.8
 */
public class ValidateUtil {

    /**
     * 获取验证器
     */
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    public void test(){
        Result result = new Result(null);
        String s = parseValidateSet(result);
        System.out.println(s);
    }
    /**
     * 解析验证返回的结果集
     *
     * @param obj 验证对象
     */
    public static  <T> String parseValidateSet(T obj) {
        Set<ConstraintViolation<T>> resultSet = VALIDATOR.validate(obj);
        StringBuilder sBuilder = new StringBuilder();
        if (resultSet != null && resultSet.size() > 0) {
            for (ConstraintViolation<T> constraint : resultSet) {
                String message = constraint.getMessage();
                if (StringUtils.isNotBlank(message)) {
                    sBuilder.append(message).append("，");
                } else {
                    sBuilder.append(constraint.getPropertyPath().toString()).append("不合法、");
                }
            }
        }
        return sBuilder.length() == 0 ? null : sBuilder.toString();
    }
}
