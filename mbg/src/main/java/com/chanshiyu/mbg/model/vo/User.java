package com.chanshiyu.mbg.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/4 16:58
 */
@Data
@NoArgsConstructor
public class User {

    private int userId;

    private String username;

    private String nickname;

    private String avatar;

    private String device;

    private boolean tourist;

}
