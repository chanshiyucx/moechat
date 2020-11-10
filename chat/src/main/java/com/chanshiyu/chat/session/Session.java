package com.chanshiyu.chat.session;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 14:24
 */
@Data
@AllArgsConstructor
public class Session {

    private long userId;

    private String username;

}
