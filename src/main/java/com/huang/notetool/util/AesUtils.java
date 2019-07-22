package com.huang.notetool.util;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 采用JDK1.8的AES加密和解密
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-03-13 15:55:33
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-03-13   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class AesUtils {
    /**
     * 加入业务日志
     */
    private static Logger logger = Logger.getLogger(AesUtils.class);

    /**
     * 加密
     * 1.密钥为空，抛出异常
     * 2.需要加密的字符串为空，抛出异常
     * 3.密钥长度不满足16位，抛出异常
     * 4.以上情况都不满足，进行加密
     *
     * @param encryptKey    密匙
     * @param encryptString 需要加密的字符串
     * @return 加密后的字符串
     * @throws Exception 抛出入参不正确的异常 和 加密过程的异常
     */
    public static String encryptByAes(String encryptKey, String encryptString) throws Exception {
        if (StringUtils.isEmpty(encryptKey)) {
            logger.warn("密匙为空[{}]" + encryptKey);
            throw new Exception("密匙为空");
        }
        if (StringUtils.isEmpty(encryptString)) {
            logger.warn("需要加密的字符串为空[{}]" + encryptString);
            throw new Exception("需要加密的字符串为空");
        }
        // 判断Key是否为16位
        if (Constants.NUMBER_SIXTY != encryptKey.length()) {
            logger.warn("密匙长度[{}]不是16位" + encryptKey.length());
            throw new Exception("密匙长度不是16位");
        }
        try {
            byte[] raw = encryptKey.getBytes(Constants.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, Constants.AES);
            //算法/模式/补码方式
            Cipher cipher = Cipher.getInstance(Constants.AES_ECB_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptString.getBytes(Constants.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.warn("加密出现异常", e);
            throw new Exception(e);
        }
    }


    /**
     * 解密
     * 1.密钥为空，抛出异常
     * 2.需要加密的字符串为空，抛出异常
     * 3.密钥长度不满足16位，抛出异常
     * 4.以上情况都不满足，进行解密
     *
     * @param decryptKey    密匙
     * @param decryptString 加密字符串
     * @return 解密后的字符串
     * @throws Exception 可能出现的异常
     */
    public static String decryptByAes(String decryptKey, String decryptString) throws Exception {
        // 判断Key是否正确
        if (StringUtils.isEmpty(decryptKey)) {
            throw new Exception("密钥为空");
        }
        if (StringUtils.isEmpty(decryptString)) {
            throw new Exception("需要加密的字符串为空");
        }
        // 判断Key是否为16位
        if (Constants.NUMBER_SIXTY != decryptKey.length()) {
            throw new Exception("密钥长度不是16位");
        }
        try {
            byte[] raw = decryptKey.getBytes(Constants.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, Constants.AES);
            //算法/模式/补码方式
            Cipher cipher = Cipher.getInstance(Constants.AES_ECB_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(decryptString);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, Constants.UTF_8);
        } catch (Exception e) {
            logger.warn("解密异常" + e);
            throw new Exception(e);
        }
    }

    /**
     * md5加密
     *
     * @param encryptMsg
     * @return
     */
    public static String md5Encrypt(String encryptMsg) throws Exception {
        //定义一个字节数组
        byte[] secretBytes;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            messageDigest.update(encryptMsg.getBytes());
            //获得加密后的数据
            secretBytes = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e);
            throw new NoSuchAlgorithmException(e);
        }
        //将加密后的数据转换为16进制数字
        // 16进制数字
        StringBuilder md5code32 = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        logger.info(md5code32);
        logger.info(new BigInteger(1, secretBytes).toString(32));
        StringBuilder returnValue = new StringBuilder("16位：");
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code32.length(); i++) {
            md5code32.insert(0, "0");
        }
        returnValue.append(md5code32.substring(8, 24));
        //换行
        returnValue.append(System.getProperty("line.separator"));
        returnValue.append("32位：");
        returnValue.append(md5code32);
        return returnValue.toString();
    }

}