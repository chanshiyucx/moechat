package com.chanshiyu.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author SHIYU
 * @description 全局跨域配置
 * @since 2020/4/9 9:37
 */
@Configuration
public class CorsConfig {

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        config.addAllowedOrigin("*");
        // 允许跨越发送cookie
        config.setAllowCredentials(true);
        // 允许所有请求方法跨域调用
        config.addAllowedMethod("*");
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        // 添加映射路径，“/**” 表示对所有的路径实行全局跨域访问权限的设置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
