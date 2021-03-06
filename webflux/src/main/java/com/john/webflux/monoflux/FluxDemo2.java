package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
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
        Flux<Integer> longFlux = Flux.range(0, 25).takeWhile(item -> item < 15);
        longFlux.subscribe(item -> log.info("takeWhile={}", item));

        //takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
        TimeUnit.SECONDS.sleep(1000);

    }

    /**
     * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
     * 累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
     */
    @Test
    public void reduce() {
        Mono<Integer> reduce = Flux.range(0, 10).reduce(Integer::sum);
        reduce.subscribe(item -> log.info("累加结果={}", item));

        //带初始值
        Mono<Integer> reduce1 = Flux.range(0, 10).reduce(10, Integer::sum);
        reduce1.subscribe(item -> log.info("带初始值累加结果={}", item));

        //初始值来自于一返回值个函数
        Mono<Integer> integerMono = Flux.range(0, 10).reduceWith(() -> 5, Integer::sum);
        integerMono.subscribe(item -> log.info("初始值来自于一返回值个函数={}", item));

    }

    /**
     * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。不同之处在于 merge 按照所有流中元素的实际产生
     * 顺序来合并，而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并
     */
    @Test
    public void merge() throws InterruptedException {
        Flux<Long> take = Flux.interval(Duration.ofMillis(100)).take(5);
        Flux<Integer> range = Flux.range(1000, 100);

        //merge会按照所有流中元素的实际产生顺序来合并
        Flux<? extends Number> merge = Flux.merge(take, range);
        merge.subscribe(item -> log.info("合并结果序列的元素={}", item));

        //mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并,所以这里他会先合并所有第一个流的元素
        Flux<? extends Number> flux = Flux.mergeSequential(take, range);
        flux.subscribe(item -> log.info("mergeSequential合并结果序列的元素={}", item));


        TimeUnit.SECONDS.sleep(1000);
    }

    /**
     * flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。
     * flatMapSequential 和 flatMap 之间的区别与 mergeSequential 和 merge 之间的区别是一样的。
     */
    @Test
    public void flatMap() {
        log.info("并行线程={}", Runtime.getRuntime().availableProcessors());
//        Flux<Integer> integerFlux = Flux.just(, 4).flatMap(item -> Flux.range(0, item));
        //这里我是根据对java Stream API里面的flatmap使用场景来理解 flux的flatmap
        Integer[] integers = {1, 2};
        Integer[] integers2 = {4, 5, 6};
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Flux<Integer[]> generate = Flux.generate(LinkedList::new, (list, sink) -> {
            if (list.size() > 10) {
                sink.complete();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Integer[] ar = new Integer[2];
            ar[0] = random.nextInt(5);
            ar[1] = random.nextInt(15);
            list.add(ar);
            sink.next(ar);
            return list;
        });

        //可以设置并行度
        Flux<Integer> integerFlux2 = Flux.just(integers2, integers2).flatMap(Flux::fromArray, 2);
        integerFlux2.subscribe(item -> log.info("flatmap={}", item));
//        integerFlux.flatMapSequential()
    }

    /**
     * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。与 flatMap 不同的是，
     * concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；与 flatMapSequential 不同的是，
     * concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
     */
    @Test
    public void concatMap() throws InterruptedException {
        Flux<Long> longFlux = Flux.range(2, 5)
                .concatMap(item -> Flux.interval(Duration.ofMillis(item * 10)).take(item));
        longFlux.subscribe(item -> log.info("concatMap={}", item));

        TimeUnit.SECONDS.sleep(100);

    }

    /**
     * combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
     * 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
     */
    @Test
    public void combineLatest() throws InterruptedException {
        Flux<String> stringFlux = Flux.combineLatest(item -> Arrays.toString(item),
                Flux.interval(Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(6));
        stringFlux.subscribe(item->log.info("combinelatest={}", item));

        TimeUnit.SECONDS.sleep(100);

    }
}
