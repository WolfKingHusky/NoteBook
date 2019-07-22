package com.huang.notetool.tool;

import com.huang.notetool.po.SystemInstallMsg;
import com.huang.notetool.service.SystemInstallMsgService;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author huang
 */
public class SpecialSearch implements Runnable {
    /**
     * logger
     */
    Logger logger = Logger.getLogger(SpecialSearch.class);
    /**
     * Service
     */
    private SystemInstallMsgService systemInstallMsgService;
    /**
     * 成功与否的返回
     */
    public boolean success;
    /**
     * 入参
     */
    private String msg;

    public SpecialSearch(String content, boolean first) {
        msg = content;
    }

    /**
     * 构造方法
     *
     * @param content 搜索内容
     */
    public SpecialSearch(String content) {
        logger.info("准备打开" + content + " ....");
        systemInstallMsgService = new SystemInstallMsgService();
        CheckSystemInstallMsg checkSystemInstallMsg = new CheckSystemInstallMsg();
        List<SystemInstallMsg> systemInstallMsgList = systemInstallMsgService.getAll();
        if (null == systemInstallMsgList || systemInstallMsgList.isEmpty()) {
            //更新数据表信息
            checkSystemInstallMsg.reLoadSystemInstallMsg();
        }

        String fileAbsolutePath = null;
        if ("music".equalsIgnoreCase(content)) {
            String[] keyList = {"KuGou", "酷狗", "酷我", "wmplayer", "Player", "酷狗音乐"};
            fileAbsolutePath = getInstallMusicAbsolutePath(keyList);
        }
        if (content.equalsIgnoreCase("game")) {
            return;
        }
        if ("movie".equalsIgnoreCase(content)) {
            String[] keyList = {"YoukuDesktop", "优酷", "爱奇艺", "腾讯视频", "土豆", "搜狐视频", "迅雷影音", "xfplay"};
            fileAbsolutePath = getInstallMovieAbsolutePath(keyList);
        }
        try {
            if (StringUtils.isNotEmpty(fileAbsolutePath)) {
                Runtime.getRuntime().exec(fileAbsolutePath);
                logger.info( "打开成功.");
                success = true;
                return;
            }
            success = false;
            JOptionPane.showMessageDialog(null, "打开失败.");
            logger.warn("打开失败");
        } catch (IOException e) {
            logger.warn(e);
            success = false;
            JOptionPane.showMessageDialog(null, "打开失败.");
            logger.warn("打开失败");
        }
    }

    private String getInstallMovieAbsolutePath(String[] keyList) {
        //String[] keyList = { "优酷", "爱奇艺", "腾讯视频", "土豆", "搜狐视频", "迅雷影音", "影音先锋"};
        //文件路径 相当于\
        String fileSep = System.getProperty("file.separator");
        SystemInstallMsg systemInstallMsg = systemInstallMsgService.getMsgByKeyList(Arrays.asList(keyList));
        if (null != systemInstallMsg) {
            String unInstallAddr = systemInstallMsg.getUnInstallAddr();
            String installAddr = systemInstallMsg.getInstallAddr();
            if (systemInstallMsg.getName().contains("优酷")
                    || systemInstallMsg.getName().contains("YoukuDesktop")) {
                return getYouKuPlayAddr(fileSep, installAddr, unInstallAddr);
            }
            if (systemInstallMsg.getName().contains("腾讯视频")
                    || systemInstallMsg.getName().contains("QQLive")) {
                return getQQLiveAddr(fileSep, installAddr, unInstallAddr);
            }
            if (systemInstallMsg.getName().contains("影音先锋")
                    || systemInstallMsg.getName().contains("xfplay")) {
                return getXfplayAddr(fileSep, installAddr, unInstallAddr);
            }
            if (systemInstallMsg.getName().contains("爱奇艺视频")
                    || systemInstallMsg.getName().contains("QyClient")) {
                return getQyClientAddr(fileSep, installAddr, unInstallAddr);
            }
            if (systemInstallMsg.getName().contains("迅雷影音")
                    || systemInstallMsg.getName().contains("XLLiveUD")) {
                return getXLLiveUDAddr(fileSep, installAddr, unInstallAddr);
            }
        }
        return null;
    }

    /**
     * 获取迅雷影音安装地址
     *
     * @param fileSep       文件分隔符
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载地址
     * @return 安装路径
     */
    private String getXLLiveUDAddr(String fileSep, String installAddr, String unInstallAddr) {
        String displayName = "XLLiveUD";
        return getInstallSoftwareAddr(fileSep, installAddr, unInstallAddr, displayName);
    }

    /**
     * 获取爱奇艺视频地址
     *
     * @param fileSep       文件分隔符
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载地址
     * @return 安装路径
     */
    private String getQyClientAddr(String fileSep, String installAddr, String unInstallAddr) {
        String displayName = "QyClient";
        return getInstallSoftwareAddr(fileSep, installAddr, unInstallAddr, displayName);
    }

    /**
     * 获取影音先锋安装地址
     *
     * @param fileSep       文件分隔符号
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载地址
     * @return 安装路径
     */
    private String getXfplayAddr(String fileSep, String installAddr, String unInstallAddr) {
        String displayName = "xfplay";
        return getInstallSoftwareAddr(fileSep, installAddr, unInstallAddr, displayName);
    }

    /**
     * 获取腾讯视频安装地址
     *
     * @param fileSep       文件分隔符号
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载地址
     * @return 启动路径
     */
    private String getQQLiveAddr(String fileSep, String installAddr, String unInstallAddr) {
        String installName = "QQLive";
        return getInstallSoftwareAddr(fileSep, installAddr, unInstallAddr, installName);
    }

    /**
     * 获取优酷安装地址
     *
     * @param fileSep       文件分隔
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载地址
     * @return 启动路径
     */
    private String getYouKuPlayAddr(String fileSep, String installAddr, String unInstallAddr) {
        String installName = "YoukuDesktop";
        return getInstallSoftwareAddr(fileSep, installAddr, unInstallAddr, installName);
    }

    /**
     * 获取安装软件地址
     *
     * @param fileSep       文件分隔符号
     * @param installAddr   安装地址
     * @param unInstallAddr 卸载路径
     * @param installName   软件启动名称
     * @return 地址
     */
    private String getInstallSoftwareAddr(String fileSep, String installAddr, String unInstallAddr, String installName) {
        if (StringUtils.isNotEmpty(installAddr)) {
            return installAddr + fileSep + installName;
        } else if (StringUtils.isNotEmpty(unInstallAddr)
                //不包含360修改过的注册表
                && !unInstallAddr.contains("SoftMgr.exe")
                //不包含系统卸载
                && !unInstallAddr.contains("MsiExec.exe")
        ) {
            if (unInstallAddr.trim().lastIndexOf(".exe") + 4 >= unInstallAddr.length() - 1) {
                return unInstallAddr.substring(0, unInstallAddr.lastIndexOf(fileSep)).replaceAll("\"", Constants.EMPTY_STR) + fileSep + installName;
            } else {
                return unInstallAddr.replaceAll("\"", Constants.EMPTY_STR) + fileSep + installName;
            }
        }
        return null;
    }

    /**
     * 查找音乐
     *
     * @param keyList 关键字列表
     * @return 启动路径
     */
    private String getInstallMusicAbsolutePath(String[] keyList) {
        String installName = null;
        String fileAbsolutePath = null;
        //文件路径 相当于\
        String fileSep = System.getProperty("file.separator");
        SystemInstallMsg systemInstallMsg = systemInstallMsgService.getMsgByKeyList(Arrays.asList(keyList));
        if (null != systemInstallMsg) {
            String unInstallAddr = systemInstallMsg.getUnInstallAddr();
            if (systemInstallMsg.getName().contains("酷狗")
                    || systemInstallMsg.getName().contains("KuGou")
            ) {
                fileAbsolutePath = getKuGouInstallPath(fileAbsolutePath, fileSep, systemInstallMsg, unInstallAddr);
            } else if (systemInstallMsg.getName().contains("酷我")) {
                fileAbsolutePath = getKuWoInstallPath(fileAbsolutePath, fileSep, systemInstallMsg, unInstallAddr);
            } else {
                if (systemInstallMsg.getName().contains("wmplayer")) {
                    installName = "wmplayer.exe";
                    if (null != systemInstallMsg.getInstallAddr() && !"".equals(systemInstallMsg.getInstallAddr())) {
                        fileAbsolutePath = systemInstallMsg.getInstallAddr() + fileSep + installName;
                    }
                } else {
                    fileAbsolutePath = "C:\\Program Files\\Windows Media Player\\" + installName;
                }
            }
            return fileAbsolutePath;
        } else {
            //TODO:查询接口调用
            JOptionPane.showMessageDialog(null, "没找到", "WARN", 0, null);
        }
        return null;
    }

    private String getKuWoInstallPath(String fileAbsolutePath, String fileSep, SystemInstallMsg systemInstallMsg, String unInstallAddr) {
        String installName;
        installName = "酷我.exe";
        if (null != systemInstallMsg.getInstallAddr() && !"".equals(systemInstallMsg.getInstallAddr())) {
            fileAbsolutePath = systemInstallMsg.getInstallAddr() + fileSep + installName;
        } else if (StringUtils.isNotEmpty(unInstallAddr)
                //不包含360修改过的注册表
                && !unInstallAddr.contains("SoftMgr.exe")
                //不包含系统卸载
                && !unInstallAddr.contains("MsiExec.exe")
        ) {
            if (unInstallAddr.trim().lastIndexOf(".exe") + 4 >= unInstallAddr.length() - 1
                    //不包含360修改过的注册表
                    && !unInstallAddr.contains("SoftMgr.exe")
                    //不包含系统卸载
                    && !unInstallAddr.contains("MsiExec.exe")
            ) {
                fileAbsolutePath = unInstallAddr.substring(0, unInstallAddr.lastIndexOf(fileSep)) + fileSep + installName;
            } else {
                fileAbsolutePath = systemInstallMsg.getUnInstallAddr() + fileSep + installName;
            }
        }
        return fileAbsolutePath;
    }

    private String getKuGouInstallPath(String fileAbsolutePath, String fileSep, SystemInstallMsg systemInstallMsg, String unInstallAddr) {
        String installName;
        installName = "KuGou.exe";
        if (null != systemInstallMsg.getInstallAddr() && !"".equals(systemInstallMsg.getInstallAddr())) {
            fileAbsolutePath = systemInstallMsg.getInstallAddr() + fileSep + installName;
        } else if (null != unInstallAddr
                && !"".equals(unInstallAddr)
                //不包含360修改过的注册表
                && !unInstallAddr.contains("SoftMgr.exe")
                //不包含系统卸载
                && !unInstallAddr.contains("MsiExec.exe")
        ) {
            if (unInstallAddr.trim().lastIndexOf(".exe") + 4 >= unInstallAddr.length() - 1) {
                fileAbsolutePath = unInstallAddr.substring(0, unInstallAddr.lastIndexOf(fileSep)).replaceAll("\"", Constants.EMPTY_STR) + fileSep + installName;
            } else {
                fileAbsolutePath = systemInstallMsg.getUnInstallAddr().replaceAll("\"", Constants.EMPTY_STR) + fileSep + installName;
            }
        }
        return fileAbsolutePath;
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
        new SpecialSearch(msg);
    }

    public boolean isSuccess() {
        return success;
    }
}