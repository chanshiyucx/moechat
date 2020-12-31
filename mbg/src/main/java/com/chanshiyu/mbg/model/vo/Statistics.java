package com.chanshiyu.mbg.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/31 9:32
 */
@Data
@AllArgsConstructor
public class Statistics {

    private long totalRegisterUser;

    private long todayRegisterUser;

    private long totalSendMessage;

    private long todaySendMessage;

}
