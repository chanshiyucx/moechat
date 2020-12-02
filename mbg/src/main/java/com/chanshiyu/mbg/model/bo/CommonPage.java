package com.chanshiyu.mbg.model.bo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author SHIYU
 * @description 分页数据
 * @since 2020/3/31 16:54
 */
@Data
public class CommonPage<T> {

    /**
     * 响应数据
     */
    private List<T> list;

    /**
     * 描述信息
     */
    private ResultAttributes attributes;

    public CommonPage(IPage<T> page) {
        ResultAttributes attributes = new ResultAttributes();
        attributes.setPageNum(page.getCurrent());
        attributes.setPageSize(page.getSize());
        attributes.setTotal(page.getTotal());
        this.list = page.getRecords();
        this.attributes = attributes;
    }

}