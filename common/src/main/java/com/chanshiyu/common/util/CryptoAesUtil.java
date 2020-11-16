package com.chanshiyu.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/13 16:00
 */
public class CryptoAesUtil {

    public static byte[] encrypt(byte[] data, String key, String iv) throws Exception {
        return codec(data, key, iv, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt(byte[] data, String key, String iv) throws Exception {
        return codec(data, key, iv, Cipher.DECRYPT_MODE);
    }

    private static byte[] codec(byte[] data, String key, String iv, int mode) throws Exception {
        // 指定算法，模式，填充方法 创建一个 Cipher 实例
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        // 生成 Key 对象
        Key sKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        // 把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv.getBytes()));
        // 指定模式、密钥、参数，初始化 Cipher 对象
        cipher.init(mode, sKeySpec, params);
        // 执行加解密
        return cipher.doFinal(data);
    }

}
