package com.chanshiyu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author SHIYU
 * @description 错误请求异常
 * @since 2020/4/8 16:34
 */
public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}