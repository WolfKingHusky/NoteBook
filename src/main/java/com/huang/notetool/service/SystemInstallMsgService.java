package com.huang.notetool.service;

import com.huang.notetool.dao.SystemInstallMsgDao;
import com.huang.notetool.dao.impl.SystemInstallMsgDaoImpl;
import com.huang.notetool.po.SystemInstallMsg;
import com.huang.notetool.util.GenerateRandom;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 系统 安装软件信息service
 *
 * @author 黄
 */
public class SystemInstallMsgService {
    /**
     * Dao层
     */
    private SystemInstallMsgDao systemInstallMsgDao;
    private static Logger logger = Logger.getLogger(SystemInstallMsgService.class);

    public SystemInstallMsgService() {
        systemInstallMsgDao = new SystemInstallMsgDaoImpl();
    }

    /**
     * 获取信息
     *
     * @return 查找到的信息
     */
    public List<SystemInstallMsg> getAll() {
        return systemInstallMsgDao.findAll();
    }

    /**
     * 查询数据
     *
     * @param name 软件名字
     */
    private SystemInstallMsg getMsgByName(String name) {
        List<SystemInstallMsg> systemInstallMsgList = systemInstallMsgDao.findByName(name);
        if (null != systemInstallMsgList && !systemInstallMsgList.isEmpty()) {
            return systemInstallMsgList.get(0);
        }
        return null;
    }

    /**
     * 查询数据
     *
     * @param keyList 软件名字,可能的集合，以,分隔
     */
    public SystemInstallMsg getMsgByKeyList(List<String> keyList) {
        List<SystemInstallMsg> systemInstallMsgList = systemInstallMsgDao.findByKey(keyList);
        if (null != systemInstallMsgList && !systemInstallMsgList.isEmpty()) {
            return systemInstallMsgList.get(0);
        }
        return null;
    }


    /**
     * 新增信息
     *
     * @return boolean
     */
    public boolean add(List<SystemInstallMsg> systemInstallMsgList) {
        logger.info("开始封装 SystemInstallMsg 对象 ");
        boolean returnValue = false;
        for (SystemInstallMsg systemInstallMsg : systemInstallMsgList) {
            systemInstallMsg.setId(GenerateRandom.randomInt());
            returnValue = systemInstallMsgDao.save(systemInstallMsg);
        }
        return returnValue;
    }


    /**
     * 导出数据
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @return 成功与否
     */
    public boolean exportException(String filePath, String fileType) {
//        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileType)) {
//            return false;
//        }
//        String[] exceptionWays = null;
//        if (null != exceptionWays && exceptionWays.length > 0) {
//            return ChooseWayFrame.exportChooseFile(filePath, fileType, exceptionWays, ExceptionWay.class);
//        }
        return false;
    }

    public boolean deleteById(String id) {
        return systemInstallMsgDao.deleteById(Integer.parseInt(id));
    }

    /**
     * 更新信息
     *
     * @return boolean
     */
    public boolean updateException(List<SystemInstallMsg> systemInstallMsgList) {
        logger.info("更新 SystemInstallMsg 数据 ");
        boolean returnValue = false;
        for (SystemInstallMsg systemInstallMsg : systemInstallMsgList) {
            SystemInstallMsg dbValue = getMsgByName(systemInstallMsg.getName());
            if (null == dbValue) {
                systemInstallMsg.setId(GenerateRandom.randomInt());
                returnValue = systemInstallMsgDao.save(systemInstallMsg);
            } else {
                systemInstallMsg.setId(dbValue.getId());
                returnValue = systemInstallMsgDao.update(systemInstallMsg);
            }
        }
        return returnValue;
    }

    public void deleteAll() {
        systemInstallMsgDao.deleteAll();
    }
}
