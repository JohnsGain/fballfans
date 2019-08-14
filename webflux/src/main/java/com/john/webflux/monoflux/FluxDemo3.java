package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-08-15 00:16
 * @since jdk1.8
 */
@Slf4j
public class FluxDemo3 {

    /**
     * 当需要处理 Flux 或 Mono 中的消息时，如之前的代码清单所示，可以通过 subscribe 方法来添加相应的订阅逻辑。
     * 在调用 subscribe 方法时可以指定需要处理的消息类型。可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息
     */
    @Test
    public void subscribe() throws InterruptedException {

        Flux<Integer> concat = Flux.just(1, 2).concatWith(Mono.error(new IllegalStateException("测试错误消息")));
        concat.subscribe(System.out::println, System.err::println);

        //        正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。第一种策略是通过 onErrorReturn()方法返回一个默认值

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Flux<Object> flux = Flux.generate((sink) -> {
            int nextInt = random.nextInt(50);
            if (nextInt > 35 && nextInt < 40) {
                sink.error(new RuntimeException("bbbb"));
            } else {
                sink.next(nextInt);
            }
        }).onErrorReturn("bbb");

        flux.subscribe(item -> log.info("只接受onNext信号={}", item));
        flux.subscribe(item -> log.info("第一个函数接受onNext信号={}", item),
                error -> log.info("第二个接受error信号={}", error.getMessage()));

//        第二种策略是通过 switchOnError()方法来使用另外的流来产生元素。在代码清单 17 中，当出现错误时，
//        将产生 Mono.just(0)对应的流，也就是数字 0。

        TimeUnit.SECONDS.sleep(1000);
    }

}
