package com.jiazhong.filters;

import com.alibaba.fastjson.JSON;
import com.jiazhong.commons.Result;
import com.jiazhong.utils.JWTUtils;
import com.jiazhong.utils.RSAUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义的基础过滤器，该过滤器继承SpringSecurity中定义的BasicAuthenticationFilter
 */

public class OfficeBasicAuthenticationFilter extends BasicAuthenticationFilter {
    public OfficeBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 内部过滤方法
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            System.out.println("OfficeBasicAuthenticationFilter--->doFilterInternal....");
            //获得要放行的路径
            String servletPath = request.getServletPath();
            if (servletPath.equals("/login/captcha")){
                chain.doFilter(request,response);
                return;
            }

            //解签Token
            //获得客户端提交的Token(Authorization)
            String token = request.getHeader("Authorization");
            System.out.println(token);
            if (token == null || token.equals("")){
                System.out.println("----------------------");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println(JSON.toJSON(Result.fail(401,"请登录后访问")));
                return;
            }
            //获得公钥
            PublicKey publicKey = RSAUtils.getPublicKey("D:\\java\\上课资料\\项目-jiazhong-office\\上课资料\\登录实现\\14.6 使用过滤器实现认证成功功能\\源代码\\jiazhong-office-server\\security-server\\src\\main\\java\\com\\jiazhong\\keys\\jiazhong-office-key.pub");
            //使用公钥对Token进行解签，获得Token的负载数据

            Map payLoadData = JWTUtils.getPayLoadFromToken(token, publicKey, Map.class);

            if (payLoadData!= null && payLoadData.size()!=0){
                /***
                 *向SecurityContext中设置认证成功的上下文对象
                 * SpringSecrity通过检测SecurityContextHolder上下文对象中是否存在认证信息，来判断用户是否已认证
                 */
                 SecurityContext context = SecurityContextHolder.createEmptyContext();
                //获得负载数据中的权限列表
                List<Map<String,String>> authorityList = (List<Map<String, String>>) payLoadData.get("authorities");

                List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
                //遍历权限列表，将权限列表中的权限字符串取出，封装到SimpleGrantedAuthority对象，最后将SimpleGrantedAuthority添加到list集合中
                for (Map<String,String> map :authorityList){
                    String role = map.get("authority");
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    grantedAuthorities.add(authority);

                }
                //创建UsernamePasswordAuthenticationToken对象
                UsernamePasswordAuthenticationToken authResult =
                            new UsernamePasswordAuthenticationToken(
                                    payLoadData.get("curAccount"),
                                    null,
                                    grantedAuthorities

                            );
                //将UsernamePasswordAuthenticationToken对象添加到Security的上下文对象中
                context.setAuthentication(authResult);
                //将上下文对象添加到SecurityContextHolder中
                SecurityContextHolder.setContext(context);


            }
            super.doFilterInternal(request, response, chain);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        catch (Exception e){
//            System.out.println("1111111111111111111111");
//            response.setContentType("application/json;charset=utf-8");
//            PrintWriter out = response.getWriter();
//            out.println(JSON.toJSON(Result.fail(401,"请登录后访问")));
//
//        }
    }
}
