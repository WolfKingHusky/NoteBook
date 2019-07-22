package com.huang.notetool.util;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 获取文件编码
 *
 * @author huang
 */
public class FileConvert {
    private static Logger logger = Logger.getLogger(FileConvert.class);

    /**
     * 获取文件编码
     *
     * @param file 文件
     * @return 文件编码
     */
    public static String getFileEncoding(File file) {
        String charset = "GBK";
        //首先3个字节
        byte[] first3Bytes = new byte[3];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            boolean checked = false;
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.mark(0);
            int read = bufferedInputStream.read(first3Bytes, 0, 3);
            if (read == -1) {
                logger.info("文件" + file.getAbsolutePath() + "的编码：" + charset);
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bufferedInputStream.reset();
            if (!checked) {
                // int len = 0;
                int loc = 0;

                while ((read = bufferedInputStream.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    // 单独出现BF以下的，也算是GBK
                    if (0x80 <= read && read <= 0xBF) {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bufferedInputStream.read();
                        // 双字节 (0xC0 - 0xDF)
                        if (0x80 <= read && read <= 0xBF) {
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        } else {
                            break;
                        }
                        // 也有可能出错，但是几率较小
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bufferedInputStream.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bufferedInputStream.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            logger.info("文件" + file.getAbsolutePath() + "的编码：" + charset);
            return charset;
        } catch (Exception e) {
            logger.warn("获取文件编码出错" + e);
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                logger.warn("关闭获取文件编码出错" + e);
            }
        }
        logger.info("文件" + file.getAbsolutePath() + "的编码：" + charset);
        return null;
    }
}
