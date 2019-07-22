package com.huang.notetool.util;

import com.huang.notetool.po.SystemInstallMsg;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 系统信息获取注册表
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-30 13:01:10
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-30   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SystemInstallMsgUtil {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(SystemInstallMsgUtil.class);

    /**
     * 具体查询每一个软件的详细信息
     *
     * @param string 注册表信息
     * @return 软件信息
     * @throws IOException 文件读写异常
     */
    public static String[] queryValue(String string) throws IOException {
        //软件名称
        String nameString;
        //软件版本
        String versionString;
        //出版商
        String publisherString;
        //安装路径
        String installLocation;
        //卸载路径
        String uninstallLocation;
        //安装时间
        String installDate;
        Process process = null;
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            //软件名称
            process = runtime.exec("cmd /c reg query " + string + " /v DisplayName");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            //去掉前两行无用信息
            br.readLine();
            br.readLine();
            if ((nameString = br.readLine()) != null) {
                //去掉无用信息
                nameString = "  " + nameString.replaceAll("DisplayName|REG_EXPAND_SZ|REG_SZ", "").trim();
            }

            //软件版本
            process = runtime.exec("cmd /c reg query " + string + " /v DisplayVersion");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            //去掉前两行无用信息
            br.readLine();
            br.readLine();
            if ((versionString = br.readLine()) != null) {
                //去掉无用信息
                versionString = "  " + versionString.replaceAll("DisplayVersion|REG_SZ|REG_EXPAND_SZ", "").trim();
            }
            //出版商
            process = runtime.exec("cmd /c reg query " + string + " /v Publisher");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            //去掉前两行无用信息
            br.readLine();
            br.readLine();
            if ((publisherString = br.readLine()) != null) {
                //去掉无用信息
                publisherString = "  " + publisherString.replaceAll("Publisher|REG_SZ|REG_EXPAND_SZ", "").trim();
            }
            //安装路径
            process = runtime.exec("cmd /c reg query " + string + " /v InstallLocation");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            br.readLine();
            br.readLine();//去掉前两行无用信息
            if ((installLocation = br.readLine()) != null) {
                installLocation = "  " + installLocation.replaceAll("InstallLocation|REG_SZ|REG_EXPAND_SZ", "").trim();
            }
            //安装时间
            process = runtime.exec("cmd /c reg query " + string + " /v InstallDate");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            //去掉前两行无用信息
            br.readLine();
            br.readLine();
            if ((installDate = br.readLine()) != null) {
                try {
                    installDate = "  " + new SimpleDateFormat(Constants.YYYY_MM_DD).format(
                            new SimpleDateFormat("yyyyMMdd").parse(
                                    //去掉无用信息
                                    installDate.replaceAll("InstallDate|REG_SZ|REG_EXPAND_SZ", "").trim().toString()
                            ));
                } catch (ParseException e) {
                    logger.warn("获取安装软件的日期出错" + e);
                    installDate = "";
                }
            }
            //卸载路径
            process = runtime.exec("cmd /c reg query " + string + " /v UninstallString");
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            br = new BufferedReader(inputStreamReader);
            //去掉前两行无用信息
            br.readLine();
            br.readLine();
            if ((uninstallLocation = br.readLine()) != null) {
                //去掉无用信息
                uninstallLocation = "  " + uninstallLocation.replaceAll("UninstallString|REG_EXPAND_SZ|REG_SZ", "").trim();
            }
            String[] resultString = new String[7];
            if (null != nameString
            ) {
                resultString[0] = nameString;
                resultString[1] = versionString == null ? "" : versionString;
                resultString[2] = publisherString == null ? "" : publisherString;
                resultString[3] = installLocation == null ? "" : installLocation;
                resultString[4] = installDate == null ? "" : installDate;
                resultString[5] = uninstallLocation == null ? "" : uninstallLocation;
                return resultString;
            }
            return null;
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            try {
                process.destroy();
                if (br != null) {
                    br.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * 封装数据准备入库
     *
     * @param messageArr           信息
     * @param systemInstallMsgList 列表
     */
    public static void generateMsg(String[] messageArr, List<SystemInstallMsg> systemInstallMsgList) {
        SystemInstallMsg systemInstallMsg = new SystemInstallMsg();
        //软件名称
        systemInstallMsg.setName(messageArr[0].trim());
        //版本号
        systemInstallMsg.setVersion(messageArr[1].trim());
        //出版商
        systemInstallMsg.setPublisher(messageArr[2].trim());
        //安装路径
        systemInstallMsg.setInstallAddr(messageArr[3].trim());
        //安装时间
        String installDate = messageArr[4].trim();
        if (!"".equals(installDate) && installDate.length() > 7) {
            systemInstallMsg.setInstallDate(installDate.substring(0, 4) + "-" + installDate.substring(4, 6) + "-" + installDate.substring(6));
        }
        //卸载路径
        systemInstallMsg.setUnInstallAddr(messageArr[5].trim());
        systemInstallMsg.setUpdateDate(new Date());
        systemInstallMsgList.add(systemInstallMsg);
    }
}
