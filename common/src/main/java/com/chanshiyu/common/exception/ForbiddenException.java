package com.chanshiyu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author SHIYU
 * @description 禁止访问异常
 * @since 2020/4/8 17:24
 */
public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }

}