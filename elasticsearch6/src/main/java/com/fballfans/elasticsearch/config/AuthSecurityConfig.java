package com.fballfans.elasticsearch.config;

import bb.Aocnfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
@Import(value = Aocnfig.class)
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Aocnfig aocnfig;

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
        http.cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();

//        http.addFilter(preAuthenticationFilter())
//                .formLogin()
//                .loginPage("/auth/needlogin")
//                .loginProcessingUrl(CommonConst.AUTH_FORM)
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(logoutSuccessHandler)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/auth/needlogin", "/hello", CommonConst.IMAGE_URL, "/error",
//                        CommonConst.AUTH_FORM, CommonConst.ICON)
//                .permitAll()
//                //.anyRequest()
//                //.authenticated();
//                .and()
//                .csrf()
//                .disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()


    }

}
