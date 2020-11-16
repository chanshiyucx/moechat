package com.chanshiyu.api.security.component;

import cn.hutool.json.JSONUtil;
import com.chanshiyu.mbg.model.vo.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SHIYU
 * @description 当未登录或者 token 失效访问接口时，自定义的返回结果
 * @since 2020/4/9 14:52
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(e.getMessage())));
        httpServletResponse.getWriter().flush();
    }

}
