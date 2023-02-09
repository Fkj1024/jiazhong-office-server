package com.jiazhong.service;

import java.util.Map;

public interface LoginService {
    public void  saveCaptchaForRedis(String key, String value,long expire);
    public String getCaptchaForRedis(String key);
    public void delCaptchaForRedis(String key);

    public void  saveTokenForRedis(String key, Map<String,Object> value, long expire);

    public void delTokenForRedis(String key);
}
