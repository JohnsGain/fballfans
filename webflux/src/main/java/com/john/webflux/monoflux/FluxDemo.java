package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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

    /**
     * buffer bufferTimeOut
     * 这两个操作符的作用是把当前流中的元素收集到集合中，并把集合对象作为流中的新元素。在进行收集时可以指定不同的条件：
     * 所包含的元素的最大数量或收集的时间间隔。方法 buffer()仅使用一个条件，而 bufferTimeout()可以同时指定两个条件。
     * 指定时间间隔时可以使用 Duration 对象或毫秒数，即使用 bufferMillis()或 bufferTimeoutMillis()两个方法。
     * 除了元素数量和时间间隔之外，还可以通过 bufferUntil 和 bufferWhile 操作符来进行收集。这两个操作符的参数是表示
     * 每个集合中的元素所要满足的条件的 Predicate 对象。bufferUntil 会一直收集直到 Predicate 返回为 true。使得
     * Predicate 返回 true 的那个元素可以选择添加到当前集合或下一个集合中；bufferWhile 则只有当 Predicate 返回 true
     * 时才会收集。一旦值为 false，会立即开始下一次收集。
     */
    @Test
    public void buffer() throws InterruptedException {
//        第一行语句输出的是 5 个包含 20 个元素的数组
        Flux<List<Integer>> buffer = Flux.range(0, 100).buffer(20);
        buffer.subscribe(item -> log.info("获取元素={}", item));
        TimeUnit.SECONDS.sleep(1000);
//        第二行语句输出的是 2 个包含了 10 个元素的数组
        Flux<List<Long>> listFlux = Flux.interval(Duration.ofMillis(10))
                .buffer(Duration.ofMillis(101))
                .take(2);
        listFlux.subscribe(item -> log.info("通过时间间隔获取={}", item));
//        第三行语句输出的是 5 个包含 2 个元素的数组。每当遇到一个偶数就会结束当前的收集

        TimeUnit.SECONDS.sleep(1000);
    }

    /**
     * bufferUntil 会一直收集直到 Predicate 返回为 true,然后又开始下一轮收集
     */
    @Test
    public void bufferUntil() {
//        第三行语句输出的是 5 个包含 2 个元素的数组。每当遇到一个偶数就会结束当前的收集；
        Flux<List<Integer>> listFlux = Flux.range(0, 10).bufferUntil(item -> item % 2 == 0);
        listFlux.subscribe(item -> log.info("bufferUntil={}", item));
    }

    /**
     * bufferWhile 则只有当 Predicate 返回 true 时才会收集。一旦值为 false，会立即开始下一次收集
     */
    @Test
    public void bufferWhile() {
        Flux<List<Integer>> listFlux = Flux.range(0, 10).bufferWhile(item -> item % 2 == 0);
        listFlux.subscribe(item -> log.info("bufferWhile={}", item));
    }

    /**
     * 对流中包含的元素进行过滤，只留下满足 Predicate 指定条件的元素。代码清单 6 中的语句输出的是 1 到 10 中的所有偶数。
     */
    @Test
    public void filter() {
        Flux<Integer> filter = Flux.range(0, 10).filter(item -> item % 2 == 0);
        filter.subscribe(item -> log.info("过滤之后的序列元素={}", item));
    }

    /**
     * window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>。
     * 在代码清单 7 中，两行语句的输出结果分别是 5 个和 2 个 UnicastProcessor 字符。
     */
    @Test
    public void window() {
        Flux<Flux<Integer>> window = Flux.range(0, 100).window(20);
//        window.subscribe(item -> item.subscribe(data -> log.info("window序列={}", data)));
        window.subscribe(item -> log.info("flux序列={}", item));
    }

    /**
     * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。在合并时可以不做任何处理，由此得到的是
     * 一个元素类型为 Tuple2 的流；也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
     */
    @Test
    public void zipWith() {
//        两个流中包含的元素分别是 a，b 和 c，d。第一个 zipWith 操作符没有使用合并函数，因此结果流中的元素类型为
//        Tuple2；第二个 zipWith 操作通过合并函数把元素类型变为 String。
        Flux<Tuple2<String, String>> tuple2Flux = Flux.just("a", "b").zipWith(Flux.just("1", "2"));
        tuple2Flux.subscribe(item -> log.info("zipwith={}", item));

        //使用合并函数
        Flux<String> stringFlux = Flux.just("a", "b").zipWith(Flux.just("1", "2"), (a, b) -> a.concat("==").concat(b));
        stringFlux.subscribe(item-> log.info("使用合并函数后的序列={}", item));
    }
}
