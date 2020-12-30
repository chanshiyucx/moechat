package com.chanshiyu.chat.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private String avatar;

    private String ip;

    private String device;

    private boolean tourist;

    private Date onlineDate;

}
