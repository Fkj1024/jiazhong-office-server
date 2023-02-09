package com.jiazhong.handlers;

import com.alibaba.fastjson.JSONObject;
import com.jiazhong.commons.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证失败处理器
 */
public class OfficeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        System.out.println("OfficeAuthenticationFailureHandler---->onAuthenticationFailure");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        /*System.out.println(exception.getClass());

        if(exception instanceof BadCredentialsException){
            out.println("密码错误!");

        }else if(exception instanceof UsernameNotFoundException){
            out.println("账户名不存在");
        }else if(exception instanceof InternalAuthenticationServiceException){
            out.print(exception.getMessage());

        }*/
        if(exception.getClass()==UsernameNotFoundException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户名不存在")));
        }else if(exception.getClass()==BadCredentialsException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"密码错误")));
        }else if(exception.getClass() == LockedException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户被冻结")));
        }else if(exception.getClass() == DisabledException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户不可用")));

        }
    }
}
