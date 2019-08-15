package com.john.webflux.monoflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
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

//        ThreadLocalRandom random = ThreadLocalRandom.current();
//        Flux<Object> flux = Flux.generate((sink) -> {
//            int nextInt = random.nextInt(50);
//            if (nextInt > 35 && nextInt < 40) {
//                sink.error(new RuntimeException("bbbb"));
//            } else {
//                sink.next(nextInt);
//            }
//        }).onErrorReturn("bbb");
//
//        flux.subscribe(item -> log.info("只接受onNext信号={}", item));
//        flux.subscribe(item -> log.info("第一个函数接受onNext信号={}", item),
//                error -> log.info("第二个接受error信号={}", error.getMessage()));

//        第二种策略是是通过 onErrorResumeWith()方法来根据不同的异常类型来选择要使用的产生元素的流
        Flux<String> errorResume = Flux.just("a", "b").concatWith(Mono.error(new IllegalArgumentException("测试错误消息")))
                .onErrorResume(error -> {
                    if (error instanceof IllegalStateException) {
                        return Mono.just("0");
                    }
                    if (error instanceof IllegalArgumentException) {
                        return Mono.just("1");
                    }
                    return Mono.just("2");
                });
        errorResume.subscribe(item -> log.info("onerrorResume={}", item));

//    当出现错误时，还可以通过 retry 操作符来进行重试。重试的动作是通过重新订阅序列来实现的。在使用 retry 操作符时可以指定重试的次数。
        Flux<String> errorResume2 = Flux.just("a", "b").concatWith(Mono.error(new IllegalArgumentException("测试错误消息")));
        errorResume2.retry(2).subscribe(item -> log.info("retry={}", item));

        TimeUnit.SECONDS.sleep(1000);


    }

//    前面介绍了反应式流和在其上可以进行的各种操作，通过调度器（Scheduler）可以指定这些操作执行的方式和所在的线程。
//    有下面几种不同的调度器实现。
    /*当前线程，通过 Schedulers.immediate()方法来创建。
    单一的可复用的线程，通过 Schedulers.single()方法来创建。
    使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。
        如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
    使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器
        适用于计算密集型的流的处理。
    使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
    从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。*/

    /**
     * 在代码清单 20 中，使用 create()方法创建一个新的 Flux 对象，其中包含唯一的元素是当前线程的名称。
     * 接着是两对 publishOn()和 map()方法，其作用是先切换执行时的调度器，再把当前的线程名称作为前缀添加。
     * 最后通过 subscribeOn()方法来改变流产生时的执行方式。运行之后的结果是[elastic-2] [single-1] parallel-1。
     * 最内层的线程名字 parallel-1 来自产生流中元素时使用的 Schedulers.parallel()调度器，中间的线程名称 single-1
     * 来自第一个 map 操作之前的 Schedulers.single()调度器，最外层的线程名字 elastic-2 来自第二个 map 操作之前的
     * Schedulers.elastic()调度器。
     */
    @Test
    public void scheduler() throws InterruptedException {
        Flux<String> stringFlux = Flux.create(item -> {
            item.next(Thread.currentThread().getName());
            item.complete();
        }).publishOn(Schedulers.single())
                .map(item -> Thread.currentThread().getName() + "==" + item)
                .publishOn(Schedulers.elastic())
                .map(item -> Thread.currentThread().getName() + "::::" + item)
                .subscribeOn(Schedulers.parallel());

        stringFlux.subscribe(item -> log.info("Schedulers={}", item));

        TimeUnit.SECONDS.sleep(1000);

    }


//=================在对使用 Reactor 的代码进行测试时，需要用到 io.projectreactor.addons:reactor-test 库。
    /**
     * 进行测试时的一个典型的场景是对于一个序列，验证其中所包含的元素是否符合预期。StepVerifier
     * 的作用是可以对序列中包含的元素进行逐一验证。在代码清单 21 中，需要验证的流中包含 a 和 b 两个元素。
     * 通过 StepVerifier.create()方法对一个流进行包装之后再进行验证。expectNext()方法用来声明测试时
     * 所期待的流中的下一个元素的值，而 verifyComplete()方法则验证流是否正常结束。类似的方法还有
     * verifyError()来验证流由于错误而终止。
     */
    @Test
    public void stepVerifier() {

    }

}
