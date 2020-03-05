package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * {@link reactor.core.publisher.Mono}API的使用
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-06 08:02
 * @since jdk1.8
 */
@Slf4j
public class MonoDemo {

    /**
     * 构造 mono 的方法
     * Mono 的创建方式与之前介绍的 Flux 比较相似。Mono 类中也包含了一些与 Flux 类中相同的静态方法。
     * 这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono 还有一些独有的静态方法。
     * fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：
     * :分别从 Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
     * delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
     * ignoreElements(Publisher<T> source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
     * justOrEmpty(Optional<? extends T> data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。
     * 只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
     */
    @Test
    public void constructure() throws ExecutionException, InterruptedException {
        Mono<String> just = Mono.just("1");
        Mono<Object> empty = Mono.empty();
        Mono<Object> error = Mono.error(Throwable::new);
        Mono<Object> never = Mono.never();
        Mono<String> callable = Mono.fromCallable(() -> "bb");
        Mono<Integer> integerMono = Mono.fromSupplier(() -> 5);
//        创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
        //            item.onSubscribe();
        Mono<Object> ignoreElements = Mono.ignoreElements(Subscriber::onComplete);
        log.info("声明异步任务");
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("异步线程执行");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("睡眠");
            return "sggg";
        });
        log.info("声明异步结束");
        Mono<String> stringMono = Mono.fromFuture(completableFuture);
        log.info("继续执行");
        completableFuture.thenAcceptAsync(item -> stringMono.subscribe((data) -> log.info("获取元素={}", data)));
        completableFuture.thenAcceptAsync(item -> stringMono.subscribe((data) -> log.info("获取元素={}", data)));
        stringMono.subscribe( (item) -> log.info("获取元素111={}", item));
//        String s = completableFuture.get();
        stringMono.subscribe( (item) -> log.info("获取元素222={}", item));
        stringMono.subscribe( (item) -> log.info("获取元素333={}", item));

//        Mono.fromCompletionStage()
        TimeUnit.SECONDS.sleep(1000);
    }

    @Test
    public void create() {
        Mono<String> objectMono = Mono.create((item) -> {
            log.info("hello world!");
            item.success("hello world!");
        });
        objectMono.subscribe(item -> log.info(item+" jio"));
    }

    @Test
    public void test(){

    }
}
