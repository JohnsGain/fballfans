package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-07 00:17
 * @since jdk1.8
 */
@Slf4j
public class FluxDemo {

    /**
     * 构造flux的方法
     */
    @Test
    public void constructure() {
//        可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
        Flux<Integer> just = Flux.just(1, 2);
        log.info("just个数={}", just.count().block());
        ArrayList<String> list = Lists.newArrayList("2", "b", "4");
//        可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
        Flux<String> stringFlux = Flux.fromIterable(list);
        Flux<String> stringFlux1 = Flux.fromArray(new String[]{"a", "b"});
        Flux<String> stringFlux2 = Flux.fromStream(list.stream());
//        empty()：创建一个不包含任何元素，只发布结束消息的序列。
        Flux<Object> empty = Flux.empty();
//        error(Throwable error)：创建一个只包含错误消息的序列。
        Flux<Object> flux = Flux.error(new IllegalAccessError("内部错误"));
//        never()：创建一个不包含任何消息通知的序列。
        Flux<Object> never = Flux.never();
//        创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
        Flux<Integer> range = Flux.range(0, 10);

    /*    interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的
        Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。*/
        Flux<Long> interval = Flux.interval(Duration.ofMillis(10));
    }

    /**
     * generate()方法通过同步和逐一的方式来产生 Flux 序列。序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，
     * complete()和 error(Throwable)方法来完成的。逐一生成的含义是在具体的生成逻辑中，next()方法只能最多被调用一次。
     * 在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。此时可以使用 generate()方法的另外一种形式
     * generate(Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，其中 stateSupplier
     * 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，可以在对应的逻辑中
     * 对该状态对象进行修改以供下一次生成时使用。
     */
    @Test
    public void generate() {
        //第一个序列的生成逻辑中通过 next()方法产生一个简单的值，然后通过 complete()方法来结束该序列。
        // 如果不调用 complete()方法，所产生的是一个无限序列。
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Flux<Object> generate = Flux.generate(sink -> {
            sink.next(random.nextDouble(5));
        });
        //你会发现这里一直在消费
        generate.subscribe(item -> log.info("消费数据={}", item));
    }

    /**
     * 。第二个序列的生成逻辑中的状态对象是一个 ArrayList 对象。实际产生的值是一个随机数。产生的随机数被添加到 ArrayList 中。
     * 当产生了 10 个数时，通过 complete()方法来结束序列。
     */
    @Test
    public void generate2() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Flux<Object> generate = Flux.generate((Callable<LinkedList<Double>>) LinkedList::new, (list, sink) -> {
            double data = random.nextDouble(5);
            list.add(data);
            sink.next(data);
            if (list.size() >= 10) {
                sink.complete();
            }
            return list;
        });
        log.info("=={}", generate.count().block());
        generate.subscribe(item -> log.info("消费数据={}", item));
    }

    /**
     * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。FluxSink 支持同步和异步的消息产生，
     * 并且可以在一次调用中产生多个元素。在代码清单 3 中，在一次调用中就产生了全部的 10 个元素。
     */
    @Test
    public void create() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Flux<Object> flux = Flux.create(item -> {
            for (int i = 0; i < 5; i++) {
                item.next(random.nextDouble(5));
            }
        });

        flux.subscribe(item -> log.info("订阅数据={}", item));
    }
}
