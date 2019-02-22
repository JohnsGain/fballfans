package com.john.auth.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangjuwa
 * @date 2019/2/15
 * @since jdk1.8
 */
public class KanBanPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String SSO_TOKEN = "Authorization";
    private static final String SSO_CREDENTIALS = "N/A";

    public KanBanPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(SSO_TOKEN);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return SSO_CREDENTIALS;
    }
}
