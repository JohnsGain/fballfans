package com.john;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@ApiModel("分页对象")
public class Pageable {

    @ApiModelProperty("页码:从1开始")
    private int page = 1;

    @ApiModelProperty("每页条数:默认10")
    private int size = 10;

    @ApiModelProperty("排序规则,例:id-desc")
    private String[] sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }
}
