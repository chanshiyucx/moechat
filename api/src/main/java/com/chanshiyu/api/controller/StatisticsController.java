package com.chanshiyu.api.controller;

import com.chanshiyu.chat.util.ChatUtil;
import com.chanshiyu.mbg.model.bo.CommonResult;
import com.chanshiyu.mbg.model.vo.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/31 9:30
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @GetMapping("/detail")
    public CommonResult<Statistics> detail() {
        long totalRegisterUser = ChatUtil.getTotalRegisterUser();
        System.out.println("totalRegisterUser--" + totalRegisterUser);
        long todayRegisterUser = ChatUtil.getTodayRegisterUser();
        System.out.println("todayRegisterUser--" + todayRegisterUser);
        long totalSendMessage = ChatUtil.getTotalSendMessage();
        System.out.println("totalSendMessage--" + totalSendMessage);
        long todaySendMessage = ChatUtil.getTodaySendMessage();
        System.out.println("todaySendMessage--" + todaySendMessage);
        Statistics statistics = new Statistics(totalRegisterUser, todayRegisterUser, totalSendMessage, todaySendMessage);
        return CommonResult.success(statistics);
    }

}
