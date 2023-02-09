package com.jiazhong.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiazhong.commons.Result;
import com.jiazhong.model.CurAccount;
import com.jiazhong.model.Emp;
import com.jiazhong.model.params.LoginParams;
import com.jiazhong.service.EmpService;
import com.jiazhong.service.LoginService;
import com.jiazhong.utils.JWTUtils;
import com.jiazhong.utils.RSAUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义OfficeUsernamePasswordAuthenticationFilter过滤器类，该过滤器继承UsernamePasswordAuthenticationFilter类
 */
public class OfficeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;
    private EmpService empService;
    private LoginService loginService;

    /**
     * 定义带参数的构造方法，并将AuthenticationManager认证管理器设置给父类
     * @param authenticationManager
     */
    public OfficeUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, EmpService empService,LoginService loginService){
        super(authenticationManager);
        this.empService = empService;
        this.loginService = loginService;
    }
    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    /**
     * 重写父类中attemptAuthentication方法
     * attemptAuthentication方法在UserDetailsService执行前执行，该方法用于获得客户端发送的认证数据(用户名和密码)
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            if (this.postOnly && !request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            } else {
                System.out.println("OfficeUsernamePasswordAuthenticationFilter--->attemptAuthentication");

                //SpringMVC所使用的JSON转换器是谁?jackson是SpringMVC默认使用的JSON转换器
                LoginParams loginParams = new ObjectMapper().readValue(request.getInputStream(), LoginParams.class);
                System.out.println(loginParams.getCaptcha_code()+"-------------------------");
                if (loginParams.getCaptcha_code() == null || loginParams.getCaptcha_code().equals("")){
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.println(JSONObject.toJSON(Result.fail(401,"验证不正确")));
                    return null;
                }
                //从redis中获取验证码
                 String captcha = loginService.getCaptchaForRedis(loginParams.getCaptcha_key());
                System.out.println("----------------");
                System.out.println(captcha);
                System.out.println(loginParams.getCaptcha_code());
                System.out.println("----------------");
                //判断验证码是否正确
                if (!loginParams.getCaptcha_code().equalsIgnoreCase(captcha)){
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.println(JSONObject.toJSON(Result.fail(401,"验证不正确")));
                    return null;
                }
                //删除redis中的验证码
                loginService.delCaptchaForRedis(loginParams.getCaptcha_key());

                System.out.println(loginParams);

                //String username = this.obtainUsername(request);//获得用户名
                //loginParams.setAccount_name(loginParams.getAccount_name()!=null?loginParams.setAccount_name(loginParams.getAccount_name().trim()););username = username != null ? username.trim() : "";
                //String password = this.obtainPassword(request);//获得密码
                //password = password != null ? password : "";

                //创建认证令牌对象
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginParams.getAccount_name(), loginParams.getAccount_password());
                //将认证令牌提交给UserDetailsService处理
                this.setDetails(request, authRequest);

                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 认证成功后执行该方法，该方法在认证成功处理器执行前执行
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            System.out.println("OfficeUsernamePasswordAuthenticationFilter---->successfulAuthentication...");
            /***
             *向SecurityContext中设置认证成功的上下文对象
             * SpringSecrity通过检测SecurityContextHolder上下文对象中是否存在认证信息，来判断用户是否已认证

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            System.out.println("----------"+authResult+"------------");
            SecurityContextHolder.setContext(context);*/

            //获得认证信息
            //从认证结果中获得认证主体的username数据(该数据为当前账户对象的JSON串)
            CurAccount curAccount = JSONObject.parseObject(((User)authResult.getPrincipal()).getUsername(),CurAccount.class);
            System.out.println(curAccount);
            //根据员工编号获得员工数据
            Emp curEmp = empService.getEmpByEmpId(curAccount.getEmp_id());
            //将需要向客户端回传的数据封装到Map集合中
            Map<String,Object> curAccountMap = new HashMap<>();
            curAccountMap.put("curAccount",curAccount);
            curAccountMap.put("curEmp",curEmp);
            //获得用户的权限列表，加到map集合中
            Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
            curAccountMap.put("authorities",authorities);
            //生成Token(使用私钥生成Token)
            //获得私钥对象
            PrivateKey privateKey = RSAUtils.getPrivateKey("D:\\java\\上课资料\\项目-jiazhong-office\\上课资料\\登录实现\\14.6 使用过滤器实现认证成功功能\\源代码\\jiazhong-office-server\\security-server\\src\\main\\java\\com\\jiazhong\\keys\\jiazhong-office-key.pri");
            String token = JWTUtils.generateToken(curAccountMap, privateKey, 60 * 60 * 24 * 7);

            //将token和负载数据添加到redis中，以便于其他项目使用token获得数据
            loginService.saveTokenForRedis(token,curAccountMap,60 * 60 * 24 * 7);





            //将Map回传到客户端
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSON(Result.success("登录成功",token)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * 认证失败后执行该方法，该方法在认证失败处理器前执行(认证处理器在该方法中调用)
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                                HttpServletResponse response,
                                                    AuthenticationException exception) throws IOException, ServletException {
        System.out.println("OfficeUsernamePasswordAuthenticationFilter---->unsuccessfulAuthentication...");
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
        if(exception.getClass()== UsernameNotFoundException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户名不存在")));
        }else if(exception.getClass()== BadCredentialsException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"密码错误")));
        }else if(exception.getClass() == LockedException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户被冻结")));
        }else if(exception.getClass() == DisabledException.class){
            out.println(JSONObject.toJSON(Result.fail(401,"账户不可用")));

        }
        //super.unsuccessfulAuthentication(request, response, exception);
    }
}
