package com.john.server.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicDouble;
import com.john.server.domain.entity.Order;
import com.john.server.service.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:35
 * @since jdk1.8
 */
@Slf4j
public class ImoritTest {

    @Test
    public void test() {

        AtomicDouble atomicDouble = new AtomicDouble();

        atomicDouble.set(5);
        boolean b = atomicDouble.compareAndSet(4, 8);

        while (atomicDouble.compareAndSet(5, 6)) {
            System.out.println(atomicDouble.get());
            System.out.println(atomicDouble.addAndGet(0.5));
        }

    }

    @Test
    public void imports() {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
//        ExcelImportUtil.importExcel(
//                new File(PoiPublicUtil.getWebRootPath("import/ExcelExportMsgClient.xlsx")),
//                MsgClient.class, params);

//        List<Product> excel = ExcelImportUtil.importExcel(new File("/Users/ligeit/Desktop/工作簿1.xlsx"), Product.class, params);
//        System.out.println(excel.size());
        ExcelImportResult<Product> excel = ExcelImportUtil.importExcelMore(new File("/Users/ligeit/Desktop/工作簿1.xlsx"), Product.class, params);
        List<Product> failList = excel.getFailList();
//        System.out.println(failList.stream().map(item->item.get));
    }

    /**
     * 使用Collectios.nCopy方法
     */
    @Test
    public void nCopy() {
        Order order = new Order();
        order.setId(5L);
        List<Order> orders = Collections.nCopies(5, order);
        orders.forEach(System.out::println);

    }

    /**
     * integer直接赋值的变量，==比较，在-128和 127之间，是为true的，因为在Integer里面 对 这个范围的int 值缓存到了一个数组里面。
     * 超过这个范围的Integer 直接赋值的变量，==比较，会返回false。这个时候没有缓存，而两个赋值声明将会使用不同的引用地址。==比较
     * 会直接比较地址。
     *
     * @see Integer.IntegerCache
     */
    @Test
    public void intCompare() throws InterruptedException, IOException {

        Integer a = 5;
        Integer b = 5;
        System.out.println(a == b);

        Integer c = 129;
        Integer dc = 129;
        System.out.println(c == dc);
        boolean ge = ge();
        System.out.println(ge);

    }

    private boolean ge() throws IOException {
        try (ServerSocketChannel open = ServerSocketChannel.open();) {

        }
        boolean fla = false;
        for (; ; ) {
            if (5 == RandomUtils.nextInt(0, 9)) {
                return true;
            }
            if (9 == RandomUtils.nextInt(0, 11)) {
                return false;
            }
        }

    }

    /**
     * @see Iterator#forEachRemaining(Consumer)
     * 用于替代
     * while(iterator.hasNext()){
     * function.apply(iterator.next());
     * }
     */
    @Test
    public void foreachRemain() {
        List<String> list = new ArrayList<>();
        List<Integer> collect = new Random().ints(0, 10).limit(10).boxed()
                .collect(Collectors.toList());
        Iterator<Integer> iterator = collect.listIterator();
        iterator.forEachRemaining(item -> {
            if (item == 2) {
                iterator.remove();
                System.out.println("szdfgasgd");
            }
        });
    }

    @Test
    public void readJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List list = mapper.readValue("[\"ui_member_after_sales\",\"api_serviceOrderPagination\",\"api_updateServiceOrder\",\"api_products\",\"api_allEnabedProviders\"]", List.class);
        System.out.println(list.size());
    }

    @Test
    public void sdfgsd() throws InterruptedException {
        log.info("sdf");
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info(Thread.currentThread().getName());
        }).thenAccept((item) -> log.info("asdg"));
        log.info("zsdgsadg");

        //main线程完了进程就关了，所以要睡眠等待异步线程执行
        TimeUnit.SECONDS.sleep(200);

    }



}
