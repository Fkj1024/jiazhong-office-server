package com.jiazhong.handlers;

import com.alibaba.fastjson.JSONObject;
import com.jiazhong.commons.Result;
import com.jiazhong.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功处理器
 */
public class OfficeLogoutSuccessHandler implements LogoutSuccessHandler {
    private LoginService loginService;
    public OfficeLogoutSuccessHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OfficeLogoutSuccessHandler--------->onLogoutSuccess");
        //从redis中将token移除
        String token = request.getHeader("Authorization");
        loginService.delTokenForRedis(token);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSONObject.toJSON(Result.success("退出成功")));
    }
}
