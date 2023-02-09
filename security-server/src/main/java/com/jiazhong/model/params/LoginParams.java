package com.jiazhong.model.params;

import lombok.Data;

import java.io.Serializable;

/***
 * 用于封装登录信息的实体对象
 */
@Data
public class LoginParams implements Serializable {
    private String account_name;
    private String account_password;
    private String captcha_code;
    private String captcha_key;


}
