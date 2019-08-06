package com.john.webflux;

import com.john.webflux.domain.entity.User;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author zhangjuwa
 * @apiNote 除了服务器端实现之外，WebFlux 也提供了响应式客户端，可以访问 HTTP、SSE 和 WebSocket 服务器端。
 * @date 2019-08-06 00:27
 * @since jdk1.8
 */
public class WebClientTest {

    public static void main(String[] args) {
        User user = new User();
        user.setId(3);
        user.setName("johnsgd");
        WebClient webClient = WebClient.create("http://localhost:9203/test/id");

    }

}
