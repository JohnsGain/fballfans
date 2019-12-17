package com.john.server.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.util.concurrent.AtomicDouble;
import com.john.server.domain.entity.Order;
import com.john.server.service.dto.Product;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:35
 * @since jdk1.8
 */
public class ImoritTest {

    @Test
    public void test() {
//        AbstractQueuedSynchronizer
        AtomicDouble atomicDouble = new AtomicDouble();
        atomicDouble.set(5);
        boolean b = atomicDouble.compareAndSet(4, 8);
//        CaffeineCache

        atomicDouble.compareAndSet(5, 6);
        System.out.println(atomicDouble.get());
        System.out.println(atomicDouble.addAndGet(0.5));

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
        order.setId(5);
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
    public void intCompare() {
        Integer a = 5;
        Integer b = 5;
        System.out.println(a == b);

        Integer c = 129;
        Integer dc = 129;
        System.out.println(c == dc);
        boolean ge = ge();
        System.out.println(ge);
    }

    private boolean ge() {
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


}
