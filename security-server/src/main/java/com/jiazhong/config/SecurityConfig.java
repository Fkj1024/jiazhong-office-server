package com.jiazhong.config;

import com.jiazhong.filters.OfficeBasicAuthenticationFilter;
import com.jiazhong.filters.OfficeUsernamePasswordAuthenticationFilter;
import com.jiazhong.handlers.OfficeAuthenticationFailureHandler;
import com.jiazhong.handlers.OfficeAuthenticationSuccessHandler;
import com.jiazhong.handlers.OfficeLogoutSuccessHandler;
import com.jiazhong.service.EmpService;
import com.jiazhong.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private EmpService empService;
    @Autowired
    private LoginService loginService;
    /**
     * 定义认证逻辑
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       /* auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());*/
        auth
                .authenticationProvider(authenticationProvider());//设置认证提供者对象
    }

    /**
     * 获得认证提供者对象
     * @return
     */
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //设置显示UserNotFoundException异常
        authenticationProvider.setHideUserNotFoundExceptions(false);
        //设置userDetailsService
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 处理请求
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/login.html","/login/fail","/login/login","/login/captcha")
                        .permitAll()
           .and()
                .formLogin()
                    .loginPage("/login/login")

                    .loginProcessingUrl("/login")
                    .failureForwardUrl("/login/fail")
                    //.defaultSuccessUrl("/index.html")//认证成功后使用重定向跳转
                    //.successForwardUrl("/login/success")//认证成功后使用请求转发跳转
                    //.failureUrl("/fail.html")//重定向到页面
                    //.failureForwardUrl("/login/fail")//请求转到指定的url

//                    .failureHandler(new OfficeAuthenticationFailureHandler())//设置失败处理器对象
//                    .successHandler(new OfficeAuthenticationSuccessHandler())//认证成功处理器
                    .usernameParameter("account_name")
                    .passwordParameter("account_password")
                .and()
                    .logout()
                        .logoutUrl("/exit")//配置退出的url地址
                            .invalidateHttpSession(true)
                            .logoutSuccessHandler(new OfficeLogoutSuccessHandler(loginService))
        ;
        //创建自定义过滤器对象,并设置认证管理器对象
        OfficeUsernamePasswordAuthenticationFilter officeUsernamePasswordAuthenticationFilter = new OfficeUsernamePasswordAuthenticationFilter(this.authenticationManager(),empService,loginService);
        OfficeBasicAuthenticationFilter officeBasicAuthenticationFilter = new OfficeBasicAuthenticationFilter(this.authenticationManager());
        //向Security中添加一个过滤器
        http.addFilter(officeUsernamePasswordAuthenticationFilter);
        http.addFilter(officeBasicAuthenticationFilter);
        super.configure(http);
    }
}
