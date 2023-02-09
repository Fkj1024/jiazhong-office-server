package com.jiazhong;

import com.jiazhong.utils.RSAUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RSATest {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        //公钥路径
        String publicKeyPath = "D:/jiazhong-office-key.pub";
        //私钥路径
        String privateKeyPath = "D:/jiazhong-office-key.pri";
        RSAUtils.generateKey(
                publicKeyPath,
                privateKeyPath,
                "jiazhong-office-@$!*%-xiqoai-%%^^#",
                1024);
    }
}
