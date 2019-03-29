package com.john.auth.config;

import com.john.auth.CommonConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.io.Serializable;

/**
 * EnableGlobalMethodSecurity 注解式为了启用全局方法级安全，即使@PreAuthorize,@PostAuthorize等注解生效
 * 使用方法：@PostAuthorize ("returnObject.type == authentication.name"): 确保登录用户只能获取他自己的用户对象
 *
 * @author ""
 * @PreAuthorize("hasRole('ADMIN')")
 * @PreAuthorize("hasRole('ADMIN') AND hasRole('DBA')")
 * @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_SUPER_ADMINISTRATOR')")
 * <p>
 * {@link Secured} :@Secured({ "ROLE_DBA", "ROLE_ADMIN" }),表示方法只能够被拥有DBA 或者ADMIN 权限的用户调用
 * <p>
 * 此注释是用来定义业务方法的安全配置属性的列表。您可以在需要安全[角色/权限等]的方法上指定 @Secured，
 * 并且只有那些角色/权限的用户才可以调用该方法。如果有人不具备要求的角色/权限但试图调用此方法，将会抛出AccessDenied 异常。
 * @date 2018/9/16
 * @since jdk1.8
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AuthenticationUserDetailsService authenticationUserDetailsService;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public AuthSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, AuthenticationUserDetailsService authenticationUserDetailsService, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, LogoutSuccessHandler logoutSuccessHandler, RedisTemplate<String, Serializable> redisTemplate) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationUserDetailsService = authenticationUserDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 我们使用SessionCreationPolicy.STATELESS无状态的Session机制（即Spring不使用HTTPSession），
     * 对于所有的请求都做权限校验，这样Spring Security的拦截器会判断所有请求的Header上有没有”X-Auth-Token”。
     * 对于异常情况（即当Spring Security发现没有），Spring会启用一个认证入口：new RestAuthenticationEntryPoint。
     * 在我们这个场景下，这个入口只是简单的返回一个401即可：
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(preAuthenticationFilter())
                .formLogin()
                .loginPage("/auth/needlogin")
                .loginProcessingUrl(CommonConst.AUTH_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/needlogin", "/hello", CommonConst.IMAGE_URL, "/error",
                        CommonConst.AUTH_FORM, CommonConst.ICON)
                .permitAll()
                //.anyRequest()
                //.authenticated();
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);


//        http
//                //把社交登录的过滤器加入到spring-security过滤链
////                .apply(imoocSocialSecurityConfig)
////                .and()
//                //在登录过滤器之前添加验证码过滤器
////                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin()
//                .loginPage(browser.getLoginPage())
//                .loginProcessingUrl(CommonConst.AUTH_FORM)
//                //自定义登录成功处理器
//                .successHandler(authenticationSuccessHandler)
//                //自定义失败处理器
//                .failureHandler(authenticationFailureHandler)
//                .and()
//                //添加记住我功能,设置rememberMeTokenRepository,
//                // rememberMeToken有效期以及拿到token后用于做登录的UserDetailService
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository)
//                .tokenValiditySeconds(browser.getRememberMeSeconds())
//                .userDetailsService(userDetailsService)
//                .and()
//                .sessionManagement()
//                //session失效 的跳转页面或路径
//                .invalidSessionUrl(CommonConst.SESSION_INVALID)
//                //当前登录用户最大session数量，当设置为1的时候，同一个用户新的登录请求通过之后会把上一次登录成功创建的session失效掉
//                .maximumSessions(1)
//                //针对过期的session处理策略，可以自己实现不同的失效策略
////                .expiredSessionStrategy(new ImoocSessionInformationExpiredStrategyImpl())
//                //设置为true意味着当session数量达到最大之后，不允许新同一用户再登录
////                    .maxSessionsPreventsLogin(true)
//                .and()
//                .and()
//                //退出登录的相关配置
//                .logout()
//                //退出登录请求的处理路径，默认是logout
//                .logoutUrl("/logout")
//                //退出成功之后的处理路径或页面
//                .logoutSuccessUrl("/client-logoutsuccess.html")
//                //设置退出后需要删除的cookie
//                .deleteCookies("JSESSIONID")
////                .logoutSuccessHandler()  配置了logoutSuccessUrl就不要配置logoutSuccessHandler
//                .and()
//                .authorizeRequests()
//                .antMatchers(browser.getLoginPage(), CommonConst.AUTH_FORM,
//                        CommonConst.AUTH_PHONE, "/code/**", "/hello", browser.getLoginPage(),
//                        securityProperties.getBrowser().getSignUpPage(),
//                        CommonConst.SOCIAL_SIGNUP, "/user/regist", CommonConst.SESSION_INVALID)
//                .permitAll()
//                .antMatchers("/abcd/*").hasAnyRole("admin", "ROLE_USER")
//                //hasRole()方法里面会把角色参数加上前缀ROLE_,所以在UserDetailService里面获取用户角色信息数据要加上前缀ROLE
//                .antMatchers(HttpMethod.GET, "/bbb/*").hasRole("FARMER")
//                .antMatchers("/bbbb/**").access("hasRole('FARMER') and hasIpAddress('192.xx.xx.3')")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .csrf().disable();
        //
//                .apply(smsCodeAuthenticationSecurityConfig);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    private AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);
        return provider;
    }

    /**
     * 登录成功获取token之后，需要认证的请经过这个过滤器获取token,任何交给PreAuthenticatedAuthenticationProvider
     * 认证，PreAuthenticatedAuthenticationProvider配置了自定义的 AuthenticationUserDetailsService
     * 用于通过这里获取的token 获取用户信息
     *
     * @return
     * @throws Exception
     */
    @Bean
    public KanBanPreAuthenticationFilter preAuthenticationFilter() throws Exception {
        KanBanPreAuthenticationFilter filter = new KanBanPreAuthenticationFilter(authenticationManager());
        filter.setRedisTemplate(redisTemplate);
        //filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        //filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
        //因为默认这个过滤器认证不通过，还要继续用过滤链下面的过滤器继续认证，所以这里没有实现失败处理器
        //filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        //SaltSource saltSource = new ReflectionSaltSource();
        //provider.setSaltSource(item -> "123");
        return provider;
    }


}
