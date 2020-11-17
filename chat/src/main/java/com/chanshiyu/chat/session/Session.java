package com.chanshiyu.chat.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 14:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private int userId;

    private String username;

    private String nickname;

    private String ip;

    private Date onlineDate;

}
