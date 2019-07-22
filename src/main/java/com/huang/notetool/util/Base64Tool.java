package com.huang.notetool.util;


import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-03-14 00:03:29
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-03-14   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class Base64Tool {
    private static Logger logger = Logger.getLogger(Base64Tool.class);

    /**
     * BASE64解密
     *
     * @param msg 需要解密的字符串
     * @return 解密后的字符串
     */
    public static String decode(String msg) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        byte[] decode = Base64.getDecoder().decode(msg);
        return new String(decode, Constants.UTF_8);
    }

    /**
     * BASE64加密
     *
     * @param msg 加密前的信息
     * @return 加密后的信息
     */
    public static String encode(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }

    /**
     * BASE64加密图片
     *
     * @param bufferedImage 加密前的图片流
     * @return 加密后的信息
     */
    public static String encodeBufferImag(BufferedImage bufferedImage) {
        //加密
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        return Constants.EMPTY_STR;
    }

    /**
     * BASE64解密图片
     *
     * @param base64String 加密后的字符串
     * @return 解密后的信息
     */
    public static BufferedImage decodeBufferImag(String base64String) {
        //解密
        BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            byteArrayInputStream = new ByteArrayInputStream(bytes1);
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        return null;
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return 加密后的字符串
     */
    public static String encodeBase64File(String path) {
        FileInputStream inputFile = null;
        try {
            //将文件 转换为字符串
            File file = new File(path);
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            //字符串加密
            return new BASE64Encoder().encode(buffer);
        } catch (Exception e) {
            logger.warn("Base64加密文件失败：" + e);
        } finally {
            try {
                if (inputFile != null) {
                    inputFile.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        return null;
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code 加密的base64
     * @param targetPath 保存的文件夹路径名
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        FileOutputStream out = null;
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            out = new FileOutputStream(targetPath);
            out.write(buffer);
        } catch (Exception e) {
            logger.warn("Base64解密文件出现问题" + e.toString());
            throw new Exception(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.warn(e.toString());
            }
        }
    }
}
