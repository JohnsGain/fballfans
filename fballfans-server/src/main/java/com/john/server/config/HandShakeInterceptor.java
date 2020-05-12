package com.john.server.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 权限验证，在建立握手连接之前，根据自己具业务验证权限
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-13 18:21
 * @since jdk1.8
 */
@Component
public class HandShakeInterceptor extends HttpSessionHandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
