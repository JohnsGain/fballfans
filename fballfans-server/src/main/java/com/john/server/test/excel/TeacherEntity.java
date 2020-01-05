package com.john.server.test.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-01 16:17
 * @since jdk1.8
 */
@ExcelTarget("teacherEntity")
@Data
public class TeacherEntity {

    private String id;
    /** name */
    @Excel(name = "主讲老师_major,代课老师_absent", orderNum = "1", isImportField = "true_major,true_absent")
    private String name;

}
