package com.chanshiyu.mbg.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/16 9:49
 */
@Data
public class AdminParam {

    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

}
