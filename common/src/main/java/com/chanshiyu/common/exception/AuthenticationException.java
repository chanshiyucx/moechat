package com.chanshiyu.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author SHIYU
 * @description 认证异常
 * @since 2020/4/8 16:36
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}