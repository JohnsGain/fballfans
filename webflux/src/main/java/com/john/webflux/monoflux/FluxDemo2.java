package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 响应式API Flux学习
 *
 * @author zhangjuwa
 * @date 2019/8/13
 * @since jdk1.8
 */
@Slf4j
public class FluxDemo2 {

    /**
     * take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种
     */
    @Test
    public void take() throws InterruptedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        //take(long n)，take(Duration timespan)和 t按照指定的数量或时间间隔来提取
        Flux<Object> generate = Flux.generate(item -> {
            item.next(random.nextDouble());
        });
        generate.take(5).subscribe(item -> log.info("获取的序列元素->{}", item));
        Flux<Long> take = Flux.interval(Duration.ofMillis(100)).take(5);
        take.subscribe(item -> log.info("获取的序列元素22={}", item));

        //take(Duration timespan),按指定时间间隔获取
        Flux<Long> take1 = Flux.interval(Duration.ofMillis(100)).take(Duration.ofMillis(500));
        take1.subscribe(item -> log.info("按时间间隔获取={}", item));

        //takeLast(long n)：提取流中的最后 N 个元素,这个地方的数据流一定要是有限的数据流，否则永远也获取不到最后几个数据
        Flux<Object> objectFlux = Flux.generate(LinkedList::new, (list, sink) -> {
            int nextInt = random.nextInt(50);
            if (list.size() >= 50) {
                sink.complete();
            }
            list.add(nextInt);
            sink.next(nextInt);
            return list;
        }).takeLast(5);
        objectFlux.takeLast(5).subscribe(item -> log.info("获取最后几个={}", item));


        //takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
        Flux<Integer> integerFlux = Flux.range(0, 20).takeUntil(item -> item > 10);
        integerFlux.subscribe(item -> log.info("takeUntil={}", item));


        //takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
        //注意，这里takeWhile当predicate为true的时候，是从已经产生的序列元素里面提取
        Flux<Integer> longFlux = Flux.range(0, 25).takeWhile(item -> item <15);
        longFlux.subscribe(item -> log.info("takeWhile={}", item));

        //takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
        TimeUnit.SECONDS.sleep(1000);

    }


}
