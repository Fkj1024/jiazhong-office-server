package com.jiazhong.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author: 加中实训
 * @Date:2021/9/6 14:46
 * @Description:RSA加密解密工具类(用于生成公钥和密钥)
 */
public class RSAUtils {
    private static final int DEFAULT_KEY_SIZE = 2048;

    /**
     * 从文件中读取密钥
     *
     * @param filename 公钥保存路径，
     * @return 公钥对象
     */
    public static PublicKey getPublicKey(String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径，
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     *
     * @param bytes 字节形式的公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 获得私钥
     *
     * @param bytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文，生成rsa公钥和私钥，并写入指定的文件
     *
     * @param publicKeyFileName  公钥的存储文件(带有路径及文件名)
     * @param privateKeyFileName 私钥的存储文件(带有路径及文件名)
     * @param secret             密钥
     * @param keySize            生成密钥的长度
     */
    public static void generateKey(String publicKeyFileName, String privateKeyFileName, String secret, int keySize) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE));
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        //获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
        writeFile(publicKeyFileName, publicKeyBytes);
        //获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
        writeFile(privateKeyFileName, privateKeyBytes);
    }

    private static byte[] readFile(String fileName) throws IOException {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }
}

