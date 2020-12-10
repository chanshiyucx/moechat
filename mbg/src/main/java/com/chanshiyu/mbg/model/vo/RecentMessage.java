package com.chanshiyu.mbg.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/10 9:01
 */
@Data
@AllArgsConstructor
public class RecentMessage {

    private int id;

    private byte type;

    private List<MessageVO> messageList;

}
