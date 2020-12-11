package com.chanshiyu.mbg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author SHIYU
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Message对象", description="")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发送者ID")
    private Integer sender;

    @ApiModelProperty(value = "发生者用户名")
    private String username;

    @ApiModelProperty(value = "接收者")
    private Integer receiver;

    @ApiModelProperty(value = "接收类型<1 频道，2 群组，3 用户>")
    private Integer type;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
