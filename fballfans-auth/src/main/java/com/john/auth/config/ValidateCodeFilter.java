package com.john.auth.config;

import com.john.auth.UrlConst;
import com.john.auth.properties.SecurityProperties;
import com.john.auth.properties.ValidateCodeProperties;
import com.john.auth.properties.ValidateCodeTypeEnum;
import com.john.auth.service.IValidateCodeProcessor;
import com.john.auth.service.ValidateCodeProcessorHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @email 1047994907@qq.com
 * @date 2019/2/22
 * @auther zhangjuwa
 * @since jdk1.8
 **/
@Component
public class ValidateCodeFilter extends OncePerRequestFilter
        implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCodeFilter.class);
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 需要进行验证码校验的url以对应的校验码类型
     */
    private static Map<String, ValidateCodeTypeEnum> typeEnumMap = new ConcurrentHashMap<>(8);

    @Override
    public void afterPropertiesSet() {
        ValidateCodeProperties validateCode = securityProperties.getValidateCode();
        typeEnumMap.put(UrlConst.AUTH_FORM, ValidateCodeTypeEnum.IMAGE);
        String url = validateCode.getImageCode().getUrl();
        addUrlToMap(url, ValidateCodeTypeEnum.IMAGE);

        typeEnumMap.put(UrlConst.AUTH_PHONE, ValidateCodeTypeEnum.SMS);
        addUrlToMap(validateCode.getSmsCode().getUrl(), ValidateCodeTypeEnum.SMS);
    }

    /**
     * 查看请求url是否在需要验证码验证的map里面，有就需要验证
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeTypeEnum validateCodeType = getValidateCodeType(request);
        if (validateCodeType != null) {
            IValidateCodeProcessor codeProcessor = validateCodeProcessorHolder.findCodeProcessor(validateCodeType);
            if (codeProcessor != null) {
                codeProcessor.validate(new ServletWebRequest(request, response));
            }
        }
        filterChain.doFilter(request, response);
    }

    protected void addUrlToMap(String urlString, ValidateCodeTypeEnum typeEnum) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] strings = StringUtils.splitByWholeSeparator(urlString, ",");
            for (String url : strings) {
                typeEnumMap.put(url, typeEnum);
            }
        }
    }

    private ValidateCodeTypeEnum getValidateCodeType(HttpServletRequest request) {
        return typeEnumMap.get(request.getRequestURI());
    }
}
