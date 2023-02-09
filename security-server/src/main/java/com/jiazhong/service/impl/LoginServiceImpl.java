package com.jiazhong.service.impl;

import com.jiazhong.service.LoginService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void saveCaptchaForRedis(String key, String value,long expire) {
        redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
    }

    @Override
    public String getCaptchaForRedis(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delCaptchaForRedis(String key) {
        redisTemplate.delete(key);

    }

    @Override
    public void saveTokenForRedis(String key, Map<String, Object> value, long expire) {
        redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
    }

    @Override
    public void delTokenForRedis(String key) {
        redisTemplate.delete(key);
    }
}
