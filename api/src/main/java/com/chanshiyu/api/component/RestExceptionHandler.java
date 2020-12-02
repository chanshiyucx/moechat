package com.chanshiyu.api.component;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chanshiyu.common.exception.BaseException;
import com.chanshiyu.mbg.model.bo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Stream;

/**
 * @author SHIYU
 * @description 全局异常处理
 * @since 2020/4/8 16:38
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleGlobalException(Exception e) {
        return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 自定义异常
     *
     * @param e 具体异常
     * @return 异常消息
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResult> handleBaseException(BaseException e) {
        CommonResult commonResult = CommonResult.failed(e.getStatus().value(), e.getMessage());
        return new ResponseEntity<>(commonResult, e.getStatus());
    }

    /**
     * 普通 Restful 接口参数判断
     *
     * @param e 具体异常
     * @return 异常消息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return CommonResult.failed(getValidException(e));
    }

    /**
     * webflux 接口参数判断
     *
     * @param e 具体异常
     * @return 异常消息
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleWebExchangeBindException(WebExchangeBindException e) {
        return CommonResult.failed(getValidException(e));
    }

    /**
     * Restful 把校验异常转换为字符串
     *
     * @param ex 具体异常
     * @return 异常字符串
     */
    private String getValidException(MethodArgumentNotValidException ex) {
        Stream<String> stream = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage());
        return reduceException(stream);
    }

    /**
     * webflux 把校验异常转换为字符串
     *
     * @param ex 具体异常
     * @return 异常字符串
     */
    private String getValidException(WebExchangeBindException ex) {
        Stream<String> stream = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage());
        return reduceException(stream);
    }

    /**
     * 转换异常消息字符串
     */
    private String reduceException(Stream<String> stream) {
        return stream.reduce("", (total, item) -> {
            if (StringUtils.isNotEmpty(total)) {
                total += "\n";
            }
            return total + item;
        });
    }

}
