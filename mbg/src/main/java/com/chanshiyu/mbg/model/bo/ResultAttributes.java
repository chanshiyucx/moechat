package com.chanshiyu.mbg.model.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SHIYU
 * @description 分页描述信息
 * @since 2020/3/31 16:53
 */
@Data
@NoArgsConstructor
public class ResultAttributes {

    /**
     * 当前页数
     */
    private Long pageNum;

    /**
     * 页大小
     */
    private Long pageSize;

    /**
     * 总数
     */
    private Long total;

}
