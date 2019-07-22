package com.huang.notetool.tool;

import com.huang.notetool.po.SystemInstallMsg;
import com.huang.notetool.service.SystemInstallMsgService;
import com.huang.notetool.util.SystemInstallMsgUtil;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 检查和更新系统已安装软件信息
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-30 12:30:14
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-30   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class CheckSystemInstallMsg implements Runnable {
    private Logger logger = Logger.getLogger(CheckSystemInstallMsg.class);

    private SystemInstallMsgService systemInstallMsgService;

    public CheckSystemInstallMsg() {
        systemInstallMsgService = new SystemInstallMsgService();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        checkDate();
    }

    private boolean checkDate() {
        //系统当前毫秒数
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        try {
            long startTime = dateFormat.parse(dateFormat.format(currentDate) + " 00:00:00").getTime();
            long endTime = dateFormat.parse(dateFormat.format(currentDate) + " 03:00:00").getTime();
            if (startTime < currentTime && currentTime < endTime) {
                return reLoadSystemInstallMsg();
            } else {
//                TODO:三天更新一次
//                max(;
            }
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 更新注册表信息
     *
     * @return
     */
    public boolean reLoadSystemInstallMsg() {
        //64位
        String cmd64Str = ToolUtil.CMD_QUERY_REG_64_STR;
        //32位
        String cmd32Str = ToolUtil.CMD_QUERY_REG_32_STR;
        List<SystemInstallMsg> systemInstallMsgList = new ArrayList<>();
        getSystemInstallMsg(cmd64Str, systemInstallMsgList);
        getSystemInstallMsg(cmd32Str, systemInstallMsgList);
        if (!systemInstallMsgList.isEmpty()) {
            return systemInstallMsgService.updateException(systemInstallMsgList);
        }
        return false;
    }

    /**
     * @param cmdStr               cmd reg query 语句
     * @param systemInstallMsgList 信息列表
     */
    private void getSystemInstallMsg(String cmdStr, List<SystemInstallMsg> systemInstallMsgList) {
        BufferedReader in = null;
        Process process = null;
        InputStreamReader inputStreamReader = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmdStr);

            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            in = new BufferedReader(inputStreamReader);
            String valueString;

            while ((valueString = in.readLine()) != null) {
                process = runtime.exec("cmd /c reg query "
                        + valueString
                        + " /v DisplayName");
                String[] message = SystemInstallMsgUtil.queryValue(valueString);
                if (message != null) {
                    SystemInstallMsgUtil.generateMsg(message, systemInstallMsgList);
                }
            }
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }
}
