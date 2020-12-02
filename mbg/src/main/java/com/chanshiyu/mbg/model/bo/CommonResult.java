package com.chanshiyu.mbg.model.bo;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author SHIYU
 * @description 通用响应数据
 * @since 2020/3/31 16:54
 */
@Data
public class CommonResult<T> {

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 分页描述信息
     */
    private ResultAttributes attributes;

    private CommonResult() {
        this.status = HttpStatus.OK.value();
        this.message = "OK";
    }

    private CommonResult(T data, ResultAttributes attributes) {
        this.status = HttpStatus.OK.value();
        this.message = "OK";
        this.data = data;
        this.attributes = attributes;
    }

    private CommonResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>();
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(data, null);
    }

    public static <T> CommonResult<T> success(T data, ResultAttributes attributes) {
        return new CommonResult<>(data, attributes);
    }

    public static <T> CommonResult<T> unauthorized(String message) {
        return new CommonResult<>(HttpStatus.UNAUTHORIZED.value(), message);
    }

    public static <T> CommonResult<T> forbidden(String message) {
        return new CommonResult<>(HttpStatus.FORBIDDEN.value(), message);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static <T> CommonResult<T> failed(int status, String message) {
        return new CommonResult<>(status, message);
    }

}