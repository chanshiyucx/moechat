package com.chanshiyu.mbg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author SHIYU
 * @since 2020-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@ApiModel(value="Message对象", description="")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发送者")
    @TableField("fromId")
    private Integer fromId;

    @ApiModelProperty(value = "接收者")
    @TableField("toId")
    private Integer toId;

    @ApiModelProperty(value = "接收类型<1 频道，2 群组，3 用户>")
    @TableField("toType")
    private Integer toType;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
