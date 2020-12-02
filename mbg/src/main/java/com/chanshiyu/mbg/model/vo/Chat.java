package com.chanshiyu.mbg.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 14:52
 */
@Data
@AllArgsConstructor
public class Chat {

    private int id;

    private byte type;

    private String name;

    private String avatar;

    private LocalDateTime updateTime;

}
