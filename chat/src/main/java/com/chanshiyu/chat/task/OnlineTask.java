package com.chanshiyu.chat.task;

import com.chanshiyu.chat.util.ChatUtil;
import com.chanshiyu.chat.util.SessionUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/5 8:27
 */
@Component
public class OnlineTask {

    /**
     * 通知在线用户
     */
    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    public void onlineNotice() {
        SessionUtil.getAllChannels().forEach(ChatUtil::onlineNotice);
    }

}
