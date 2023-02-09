package com.jiazhong.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

/**
 * @Author: 加中实训
 * @Date:2021/9/6 20:20
 * @Description:JWT工具类
 * 该工具类中包含以下三个方法:
 *  1.根据用户信息生成token
 *  2.从token中获取用户信息
 *  3.判断token是否已失效
 */
public class JWTUtils {
    private static final String JWT_PAYLOAD_USER_KEY = "user";
    /**
     * 使用私钥和用户信息生成token
     * @param userInfo  用户信息(JWT的负载)
     * @param privateKey 私钥对象
     * @param expire  过期时间(单位：秒)
     * @return  生成的token
     */
    public static <T> String generateToken(T userInfo, PrivateKey privateKey, int expire) throws JsonProcessingException {
        /**
         * 设置过期时间
         */
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        //在当前时间上加上指定的时间
        calendar.add(Calendar.SECOND,expire);
        //将userInfo对象转换为JSON字符串
        String userInfoJson = new ObjectMapper().writeValueAsString(userInfo);
        //生成token
        return Jwts.builder()//获得JWT的编译器对象
                .claim(JWT_PAYLOAD_USER_KEY,userInfoJson)//设置负载,负载内容为JSON字符串
                .setExpiration(calendar.getTime())//设置过期时间
                .signWith(privateKey, SignatureAlgorithm.RS256)//设置签名，使用私钥做为签名
                .compact();//生成token
    }

    /**
     * 使用公钥获取token中的payload信息(负载信息)
     * @param token     token令牌(该令牌使用私钥生成)
     * @param publicKey 公钥，用于解签token
     * @param claType   payload信息的类型
     * @return  payload数据
     */
    public static <T> T getPayLoadFromToken(String token, PublicKey publicKey, Class<T> claType) throws JsonProcessingException {
        Jws<Claims> claims = Jwts.parserBuilder()//获得JWT解析器的编译器对象
                                    .setSigningKey(publicKey)//设置签名(公钥加密签名)
                                    .build()//获得JWT编译器对象

                                    .parseClaimsJws(token);//解析token获得JWS对象

        Claims body = claims.getBody();//获得payload数据

        String jsonStr = body.get(JWT_PAYLOAD_USER_KEY).toString();
        return new ObjectMapper().readValue(jsonStr,claType);
        
    }

}
