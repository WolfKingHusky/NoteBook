package com.huang.notetool.service;

import com.huang.notetool.dao.ExceptionWayDao;
import com.huang.notetool.dao.impl.ExceptionWayDaoImpl;
import com.huang.notetool.po.ExceptionWay;
import com.huang.notetool.tool.ChooseWayFrame;
import com.huang.notetool.tool.ReadFile;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.GenerateRandom;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.WordFilter;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 异常信息service
 *
 * @author 黄
 */
public class ExceptionService {
    /**
     * Dao层
     */
    private ExceptionWayDao exceptionDao;
    private static Logger logger = Logger.getLogger(ExceptionService.class);

    public ExceptionService() {
        exceptionDao = new ExceptionWayDaoImpl();
    }

    /**
     * 获取异常信息
     * searchMsg[0] 是type
     * searchMsg[1] 是keyWord
     * searchMsg[2] 是异常信息
     *
     * @param searchMsg 异常信息
     * @return 查找到的信息
     */
    public String getExceptionAnswer(String[] searchMsg) {
        ExceptionWay exceptionWay = new ExceptionWay();
        //类型
        String type = searchMsg[0];
        //关键字
        String keyWords = searchMsg[1];
        //异常信息
        String exceptionMsg = searchMsg[2];
        if (!type.contains("未知")) {
            exceptionWay.setType(type);
        }
        if (StringUtils.isNotEmpty(keyWords)) {
            exceptionWay.setKeyWord(keyWords);
        }
        exceptionWay.setName(WordFilter.replaceMarkTONumber(exceptionMsg));
        //.reverse()字符串反转
        List<ExceptionWay> returnExceptionMsgList = new ArrayList<>();

        //通过不同的方法查询数据
        getExceptionMsgByWays(exceptionWay, keyWords, returnExceptionMsgList);
        StringBuilder returnValue = new StringBuilder();
        //换行
        String lineSeparator = System.getProperty("line.separator");
        if (!returnExceptionMsgList.isEmpty()) {
            for (ExceptionWay exceptionWayMsg : returnExceptionMsgList) {
                returnValue.append(lineSeparator).append("问题：")
                        .append(exceptionWayMsg.getName())
                        .append(lineSeparator).append("类型：")
                        .append(exceptionWayMsg.getType())
                        .append(lineSeparator).append("答案：")
                        .append(exceptionWayMsg.getAnswer());

                returnValue.append(lineSeparator).append("原因：");
                if (StringUtils.isNotEmpty(exceptionWayMsg.getCause())) {
                    returnValue.append(exceptionWayMsg.getCause());
                } else {
                    returnValue.append("还没有找到根本原因");
                }

                returnValue.append(lineSeparator)
                        .append("描述：");
                if (StringUtils.isNotEmpty(exceptionWayMsg.getCause())) {
                    returnValue.append(exceptionWayMsg.getDescription());
                } else {
                    returnValue.append("没有描述");
                }
                returnValue.append(lineSeparator).append(lineSeparator).append(lineSeparator);
            }
            logger.info("获取的信息 [" + returnExceptionMsgList.size() + "] ");
            return returnValue.toString();
        }
        return null;
    }

    /**
     * 通过不同的方法查询数据
     *
     * @param exceptionWay           异常信息实体类
     * @param keyWords               关键字
     * @param returnExceptionMsgList 返回信息列表
     */
    private void getExceptionMsgByWays(ExceptionWay exceptionWay, String keyWords, List<ExceptionWay> returnExceptionMsgList) {
        logger.info("通过 [" + exceptionWay.toString() + "] 开始查找信息 ");
        //过类型，关键字和异常信息查询
        getExceptionMsg(exceptionWay, returnExceptionMsgList);
        //如果查不到，开始通过异常信息和类型查询
        if (null == returnExceptionMsgList || returnExceptionMsgList.isEmpty()) {
            logger.info("通过 [" + exceptionWay.toString() + "] 没有查到信息 ");
            exceptionWay.setKeyWord(null);
            getExceptionMsg(exceptionWay, returnExceptionMsgList);
            //如果查不到，开始通过异常信息查询
            //异常信息作为最关键的信息
            if (null == returnExceptionMsgList || returnExceptionMsgList.isEmpty()) {
                logger.info("通过 [" + exceptionWay.toString() + "] 没有查到信息 ");
                exceptionWay.setType(null);
                getExceptionMsg(exceptionWay, returnExceptionMsgList);
                if (null == returnExceptionMsgList || returnExceptionMsgList.isEmpty()) {
                    logger.info("通过 [" + exceptionWay.toString() + "] 没有查到信息 ");
                    exceptionWay.setName(null);
                    if (StringUtils.isNotEmpty(keyWords)) {
                        String[] keyWordArr = keyWords.split(",");
                        List<ExceptionWay> exceptionWayList = exceptionDao.findByKey(Arrays.asList(keyWordArr));
                        if (null != exceptionWayList && !exceptionWayList.isEmpty()) {
                            if (null == returnExceptionMsgList) {
                                returnExceptionMsgList = new ArrayList<>();
                            }
                            returnExceptionMsgList.addAll(exceptionWayList);
                            logger.info("通过 [" + keyWordArr.length + "] 查到信息 ");
                        }
                    }
                }
            }
            if (null != returnExceptionMsgList && !returnExceptionMsgList.isEmpty()) {
                removeRePectExceptionMsg(returnExceptionMsgList);
            }
        }
    }

    /**
     * 移除重复的数据
     *
     * @param returnExceptionMsgList 返回信息
     */
    private void removeRePectExceptionMsg(List<ExceptionWay> returnExceptionMsgList) {
        Map<String, ExceptionWay> exceptionMap = new HashMap<>();
        for (ExceptionWay exceptionWay : returnExceptionMsgList) {
            if (null != exceptionWay && StringUtils.isNotEmpty(exceptionWay.getName())) {
                exceptionMap.put(exceptionWay.getName(), exceptionWay);
            }
        }
        Collection<ExceptionWay> valueCollection = exceptionMap.values();
        //移除重复数据
        returnExceptionMsgList = new ArrayList<>(valueCollection);
    }

    /**
     * 遍历异常信息查询数据
     *
     * @param exceptionWay           异常信息实体类
     * @param returnExceptionMsgList 返回信息
     */
    private void getExceptionMsg(ExceptionWay exceptionWay, List<ExceptionWay> returnExceptionMsgList) {
        List<ExceptionWay> returnMsg = exceptionDao.findByEntity(exceptionWay);
        if (null != returnMsg && !returnMsg.isEmpty()) {
            returnExceptionMsgList.addAll(returnMsg);
        }
    }


    /**
     * 新增异常信息
     *
     * @param msg
     * @param language
     * @param type
     * @param keyWords
     * @param answerMsg
     * @param cause
     * @return
     */
    public boolean addExceptionMsg(String msg, String language, String type, String keyWords, String answerMsg, String cause) {
        logger.info("开始封装 ExceptionWay 对象 ");

        ExceptionWay exceptionWay = new ExceptionWay();
        exceptionWay.setId(GenerateRandom.randomInt());
        exceptionWay.setName(WordFilter.replaceMarkTONumber(msg));
        if (!language.contains("未知")) {
            exceptionWay.setLanguage(language);
        }
        if (!type.contains("未知")) {
            exceptionWay.setType(type);
        }
        exceptionWay.setKeyWord(WordFilter.replaceMarkTONumber(keyWords));
        exceptionWay.setAnswer(answerMsg);
        if (StringUtils.isNotEmpty(cause)) {
            exceptionWay.setCause(WordFilter.replaceMarkTONumber(cause));
        }
        exceptionWay.setUpdateDate(new Date());
        exceptionWay.setDescription("待开发");
        exceptionWay.setIsSolution(0);
        try {
            exceptionWay.setUpdateDate(java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        } catch (Exception e) {
            logger.warn("时间转换出错 ");
        }
        exceptionWay.setUseTimes(0);
        return exceptionDao.save(exceptionWay);
    }

    /**
     * 查询所有的 异常信息
     *
     * @return 异常信息
     */
    public String[] getAllExceptions() {
        logger.info("查询所有的 ExceptionWay 数据 ");
        List<ExceptionWay> exceptionWayList = exceptionDao.findAll();
        StringBuilder returnMsg = new StringBuilder();
        List<String> returnList = new ArrayList<>();
        //换行
        String lineSeparator = System.getProperty("line.separator");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //编号，异常信息，语言，类型，异常信息解决方法，关键字，根本原因，更新时间，使用次数，是否解决
        for (ExceptionWay exceptionWay : exceptionWayList) {
            returnMsg.append(exceptionWay.getId())
                    .append("，").append(exceptionWay.getName().replaceAll("[\n\t\r]", ""));
            returnMsg.append("，");
            if (StringUtils.isNotEmpty(exceptionWay.getLanguage())) {
                returnMsg.append(exceptionWay.getLanguage());
            }

            if (StringUtils.isNotEmpty(exceptionWay.getType())) {
                returnMsg.append("，").append(exceptionWay.getType());
            } else {
                returnMsg.append("，");
            }
            returnMsg.append("，").append(exceptionWay.getAnswer().replaceAll("，", ",").replaceAll("[\n\t\r]", ","))
                    .append("，").append(exceptionWay.getKeyWord().replaceAll("[\n\t\r]", ","));

            returnMsg.append("，");
            if (StringUtils.isNotEmpty(exceptionWay.getCause())) {
                returnMsg.append(exceptionWay.getCause().replaceAll("[\n\t\r]", ","));
            }

            returnMsg.append("，").append(simpleDateFormat.format(exceptionWay.getUpdateDate()))
                    .append("，使用").append(exceptionWay.getUseTimes()).append("次")
                    .append("，").append(exceptionWay.getIsSolution() == 0 ? "已解决" : "未解决");
            returnList.add(returnMsg.toString());
            returnMsg = new StringBuilder();
        }
        if (!returnList.isEmpty()) {
            return returnList.toArray(new String[0]);
        }
        return null;
    }

    /**
     * 导入数据
     *
     * @param filePath 文件绝对路径
     * @return
     */
    public String importMethod(String filePath) {
        StringBuilder returnMsg = new StringBuilder();
        logger.info("进入导入页面 ");
        List<ExceptionWay> exceptionMsgList = ReadFile.readExceptionMsg(filePath);
        int successNum = 0;
        int fileNum = 0;
        if (null != exceptionMsgList && !exceptionMsgList.isEmpty()) {
            for (ExceptionWay exceptionWay : exceptionMsgList) {
                List<ExceptionWay> exceptionWayList = getByExceptionName(exceptionWay.getName());
                if (null == exceptionWayList || exceptionWayList.isEmpty()) {
                    if (!exceptionDao.save(exceptionWay)) {
                        logger.info("导入 [" + exceptionWay.getName() + "] 到数据库失败 ");
                        returnMsg.append("导入 [").append(exceptionWay.getName()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    } else {
                        returnMsg.append("导入 [").append(exceptionWay.getName()).append("] 成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    }
                } else {
                    exceptionWay.setId(exceptionMsgList.get(0).getId());
                    if (exceptionDao.update(exceptionWay)) {
                        returnMsg.append("更新 [").append(exceptionWay.getName()).append("] 到数据库成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    } else {
                        logger.info("更新 [" + exceptionWay.getName() + "] 到数据库失败 ");
                        returnMsg.append("更新 [").append(exceptionWay.getName()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    }
                }
            }
        } else {
            logger.info("导入文件 [" + filePath + "] 失败  ");
            returnMsg.append("导入文件失败").append(Constants.LINE_SEPARATOR);
        }
        returnMsg.append(Constants.LINE_SEPARATOR).append(" 成功导入 [").append(successNum).append("] 个文件").append(Constants.LINE_SEPARATOR);
        returnMsg.append("失败导入 [").append(fileNum).append("] 个文件").append(Constants.LINE_SEPARATOR);
        return returnMsg.toString();
    }

    /**
     * 导出数据
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @return 成功与否
     */
    public boolean exportException(String filePath, String fileType) {
        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileType)) {
            return false;
        }
        String[] exceptionWays = getAllExceptions();
        if (null != exceptionWays && exceptionWays.length > 0) {
            return ChooseWayFrame.exportChooseFile(filePath, fileType, exceptionWays, ExceptionWay.class);
        }
        return false;
    }

    public boolean deleteById(String id) {
        return exceptionDao.deleteById(Integer.parseInt(id));
    }

    /**
     * 更新异常信息
     *
     * @param name
     * @param language
     * @param type
     * @param keyword
     * @param answer
     * @param cause
     * @param id
     * @return
     */
    public boolean updateException(String name, String language, String type, String keyword, String answer, String cause, String id) {
        logger.info("更新 Method 数据 ");
        ExceptionWay exceptionWay = new ExceptionWay();
        exceptionWay.setId(Integer.parseInt(id));
        exceptionWay.setName(WordFilter.replaceMarkTONumber(name));
        if (StringUtils.isNotEmpty(language) && !language.contains("未知")) {
            exceptionWay.setLanguage(language);
        }
        if (StringUtils.isNotEmpty(type) && !type.contains("未知")) {
            exceptionWay.setType(type);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            exceptionWay.setKeyWord(WordFilter.replaceMarkTONumber(keyword));
        }
        exceptionWay.setAnswer(WordFilter.replaceMarkTONumber(answer));
        if (StringUtils.isNotEmpty(cause)) {
            exceptionWay.setCause(WordFilter.replaceMarkTONumber(cause));
        }
        exceptionWay.setUseTimes(1);
        exceptionWay.setIsSolution(0);
        return exceptionDao.update(exceptionWay);
    }

    /**
     * 通过名字查询异常信息
     *
     * @param name
     * @return
     */
    public List<ExceptionWay> getByExceptionName(String name) {
        return exceptionDao.findByName(WordFilter.replaceMarkTONumber(name));
    }

    public void deleteAll() {
        exceptionDao.deleteAll();
    }
}
