package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

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

    public static void main(String[] args) {
        generate();
        //generate2();
    }

    /**
     * generate()方法通过同步和逐一的方式来产生 Flux 序列。序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，
     * complete()和 error(Throwable)方法来完成的。逐一生成的含义是在具体的生成逻辑中，next()方法只能最多被调用一次。
     * 在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。此时可以使用 generate()方法的另外一种形式
     * generate(Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，其中 stateSupplier
     * 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，可以在对应的逻辑中
     * 对该状态对象进行修改以供下一次生成时使用。
     */
    private static void generate() {
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
    private static void generate2() {
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
}
