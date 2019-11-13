package com.john.server.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.john.server.service.dto.Product;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:35
 * @since jdk1.8
 */
public class ImoritTest {

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

}
