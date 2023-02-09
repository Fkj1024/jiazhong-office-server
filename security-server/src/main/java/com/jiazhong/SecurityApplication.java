package com.jiazhong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@MapperScan("com.jiazhong.mapper")
public class SecurityApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecurityApplication.class,args);
    }

    @Bean
    public WebMvcConfigurer mvcConfigurer(){
        return new WebMvcConfigurer() {

            //添加跨域配置
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")//指定项目中那些资源可以被跨域访问
                        .allowCredentials(true)//是否允许使用凭证
                        .allowedMethods("*")//允许所有请求跨域访问
                        .allowedOrigins("http://localhost:8080")//允许那些资源可以跨域访问本应用
                ;
            }
        };
    }

}
