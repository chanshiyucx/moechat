package com.chanshiyu.api.controller;


import com.chanshiyu.mbg.model.vo.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author SHIYU
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/msg")
    public CommonResult<String> msg() {
        return CommonResult.success("OK");
    }

}
