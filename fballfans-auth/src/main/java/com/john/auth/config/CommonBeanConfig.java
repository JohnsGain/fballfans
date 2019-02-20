package com.john.auth.config;

import com.john.auth.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.social.config.annotation.EnableSocial;

/**
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/10/7
 * @Since jdk1.8
 */
@Configuration
@EnableSocial
@Order(value = 1)
public class CommonBeanConfig {


    private final SecurityProperties securityProperties;

    private final RedisConnectionFactory redisConnectionFactory;


    @Autowired
    public CommonBeanConfig(SecurityProperties securityProperties, RedisConnectionFactory redisConnectionFactory) {
        this.securityProperties = securityProperties;
        this.redisConnectionFactory = redisConnectionFactory;
    }


//    /**
//     * 把社交登录加入到spring-secuirty登录过滤链上去，注入要加入的过滤器
//     * 这个对象里面包含了过滤器{@link SocialAuthenticationFilter}
//     *
//     * @return
//     */
//    @Bean
//    public SpringSocialConfigurer imoocSocialSecurityConfig() {
//        //使用自定义的子类，重写了过滤器里面过滤器处理路径
//        ImoocSpringSocialConfigurer imoocSpringSocialConfigurer = new ImoocSpringSocialConfigurer(securityProperties.getSocial().getFilterProcessesUrl());
//        imoocSpringSocialConfigurer.signupUrl(securityProperties.getBrowser().getSignUpPage());
//        return imoocSpringSocialConfigurer;
//    }


    /**
     * 配置access_token存储工具，默认是内存配置
     * 设置默认token存储方式为redis
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "store-type", havingValue = "redis", matchIfMissing = true)
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * @return 配置登录后的加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    /**
//     * 用Jwt替换传统的字符串token
//     * 如果存储类型是jwt的时候，这个配置类里面的bean生效
//     */
//    @Configuration
//    @ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "store-type", havingValue = "jwt")
//    public static class JwtConfig {
//
//        @Value("${imooc.security.oauth2.sign-key}")
//        private String signKey;
//
//        /**
//         * 负责jwt存储的bean
//         *
//         * @return
//         */
//        @Bean
//        public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
//            return new JwtTokenStore(jwtAccessTokenConverter);
//        }
//
//        /**
//         * 负责jwt的生成逻辑
//         *
//         * @return
//         */
//        @Bean
//        public JwtAccessTokenConverter jwtAccessTokenConverter() {
//            JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
//            //设置jwt签名用的密钥,如果这个蜜月丢失，那么被人知道jwt，就可以通过这个密钥解密jwt获取用户信息
//            tokenConverter.setSigningKey(signKey);
//            return tokenConverter;
//        }
//
//        @Bean
//        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
//        public TokenEnhancer jwtTokenEnhancer() {
//            return new JwtTokenEnhancerImpl();
//        }
//
//    }

}
