package com.chanshiyu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author SHIYU
 * @description 启动器
 * @since 2020/11/9 9:35
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.chanshiyu"})
@MapperScan(basePackages = "com.chanshiyu.mbg.mapper")
@EnableScheduling
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}