package com.john.webflux.monoflux;
/*
Reactor简介：
        前面提到的 RxJava 库是 JVM 上响应式编程的先驱，也是响应式流规范的基础。RxJava 2 在 RxJava 的基础上做了很多的更新。
不过 RxJava 库也有其不足的地方。RxJava 产生于响应式流规范之前，虽然可以和响应式流的接口进行转换，但是由于底层实现的原因，使用起
来并不是很直观。RxJava 2 在设计和实现时考虑到了与规范的整合，不过为了保持与 RxJava 的兼容性，很多地方在使用时也并不直观。Reactor
则是完全基于响应式流规范设计和实现的库，没有 RxJava 那样的历史包袱，在使用上更加的直观易懂。Reactor 也是 Spring 5 中响应式编程
的基础。学习和掌握 Reactor 可以更好地理解 Spring 5 中的相关概念。

        在 Java 程序中使用 Reactor 库非常的简单，只需要通过 Maven 或 Gradle 来添加对 io.projectreactor:reactor-core
的依赖即可，目前的版本是 3.0.5.RELEASE。

Flux 和 Mono
Flux 和 Mono 是 Reactor 中的两个基本概念。Flux 表示的是包含 0 到 N 个元素的异步序列。在该序列中可以包含三种不同类型的消息通知：
正常的包含元素的消息、序列结束的消息和序列出错的消息。当消息通知产生时，订阅者中对应的方法 onNext(), onComplete()和 onError()
会被调用。Mono 表示的是包含 0 或者 1 个元素的异步序列。该序列中同样可以包含与 Flux 相同的三种类型的消息通知。Flux 和 Mono 之间
可以进行转换。对一个 Flux 序列进行计数操作，得到的结果是一个 Mono<Long>对象。把两个 Mono 序列合并在一起，得到的是一个 Flux 对象。



*/
