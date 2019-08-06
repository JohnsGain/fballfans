package com.john.webflux.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器推送事件（Server-Sent Events，SSE）允许服务器端不断地推送数据到客户端。相对于 WebSocket 而言，服务器推送事件只支持服务器端到客户端的单向数据传递。
 * 虽然功能较弱，但优势在于 SSE 在已有的 HTTP 协议上使用简单易懂的文本格式来表示传输的数据。作为 W3C 的推荐规范，SSE 在浏览器端的支持也比较广泛，除了 IE
 * 之外的其他浏览器都提供了支持。在 IE 上也可以使用 polyfill 库来提供支持。在服务器端来说，SSE 是一个不断产生新数据的流，非常适合于用响应式流来表示。在 WebFlux 中创建 SSE 的服务器端是非常简单的。
 * 只需要返回的对象的类型是 Flux<ServerSentEvent>，就会被自动按照 SSE 规范要求的格式来发送响应。
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-02 07:58
 * @since jdk1.8
 */
@RestController
public class ServerSendEventController {

}
