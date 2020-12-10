package com.chanshiyu.mbg.model.vo;

import com.chanshiyu.mbg.entity.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/8 9:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageVO extends Message {

    private String fromNickname;

    private String fromAvatar;

}
