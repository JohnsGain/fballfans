package com.john.auth.config;

import com.john.auth.UrlConst;
import com.john.auth.properties.BrowserProperties;
import com.john.auth.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

/**
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/9/16
 * @Since jdk1.8
 */
@Configuration
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        BrowserProperties browser = securityProperties.getBrowser();
        http.formLogin()
//                .loginPage("https://42.123.99.59:13000/support-front/index.html#/login")
                .loginPage("/client1-login.html")
                .loginProcessingUrl(UrlConst.AUTH_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .userDetailsService(userDetailsService)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("https://42.123.99.59:13000/support-front/index.html#/login")
                .and()
                .authorizeRequests()
                .antMatchers(browser.getLoginPage(), UrlConst.AUTH_FORM,
                        UrlConst.AUTH_PHONE, "/code/**", "/hello", "/test", "/auth/needlogin", browser.getLoginPage(),
                        securityProperties.getBrowser().getSignUpPage(),
                        UrlConst.SOCIAL_SIGNUP, "/user/regist", UrlConst.SESSION_INVALID)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


//        http
//                //把社交登录的过滤器加入到spring-security过滤链
////                .apply(imoocSocialSecurityConfig)
////                .and()
//                //在登录过滤器之前添加验证码过滤器
////                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin()
//                .loginPage(browser.getLoginPage())
//                .loginProcessingUrl(UrlConst.AUTH_FORM)
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
//                .invalidSessionUrl(UrlConst.SESSION_INVALID)
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
//                .antMatchers(browser.getLoginPage(), UrlConst.AUTH_FORM,
//                        UrlConst.AUTH_PHONE, "/code/**", "/hello", browser.getLoginPage(),
//                        securityProperties.getBrowser().getSignUpPage(),
//                        UrlConst.SOCIAL_SIGNUP, "/user/regist", UrlConst.SESSION_INVALID)
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

}
