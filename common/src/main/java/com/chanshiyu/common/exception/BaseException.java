package com.chanshiyu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author SHIYU
 * @description 基本异常类型
 * @since 2020/4/8 16:32
 */
public abstract class BaseException extends RuntimeException {

    BaseException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();

}