package com.jiazhong.controller;

import com.jiazhong.service.LoginService;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    /**
     * 获得验证码
     * @return
     */
    @GetMapping("/captcha")
    public Map<String,Object> getCaptcha(HttpServletResponse response,  String captchaKey) throws IOException {
        //生成数字+字母组合验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(130,50,5);
        //生成gif图片的验证码
        //GifCaptcha captcha = new GifCaptcha(130,50,5);
        //生成计算验证码
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,50);
        //汉字验证码
        //ChineseCaptcha captcha = new ChineseCaptcha(130,50,5);
        //设置验证码的字符类型
        //captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        //将验证码图片响应给客户端
        //specCaptcha.out(response.getOutputStream());
        Map<String,Object> map = new HashMap<String,Object>();
        //将验证码转为基于Base64的字符串
        map.put("captcha",specCaptcha.toBase64());
        System.out.println(map);
        //获得验证码信息
        String captchaResult = specCaptcha.text();
        if (captchaKey == null || captchaKey.trim().equals("")){
        //生成验证码在redis中存放的前缀
        captchaKey = UUID.randomUUID().toString();
        }

        //将前缀存放到map集合中
        map.put("captchaKey",captchaKey);
        //将验证码存入到redis中
        loginService.saveCaptchaForRedis(captchaKey,captchaResult,60);
        return map;
    }
}
