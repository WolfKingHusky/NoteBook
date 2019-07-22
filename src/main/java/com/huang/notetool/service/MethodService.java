package com.huang.notetool.service;

import com.huang.notetool.dao.MethodDao;
import com.huang.notetool.dao.impl.MethodDaoImpl;
import com.huang.notetool.po.Method;
import com.huang.notetool.tool.ChooseWayFrame;
import com.huang.notetool.tool.ReadFile;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.GenerateRandom;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 操作数据的Service
 *
 * @author 黄
 */
public class MethodService {
    public static final String OTHER_TYPE = "其他";
    private MethodDao methodDao;
    private static Logger logger = Logger.getLogger(MethodService.class);

    public MethodService() {
        methodDao = new MethodDaoImpl();
    }

    /**
     * 获取方法答案
     * String[] searchMsg = {msgKeyword, searchType};
     *
     * @param searchMsg 查询的数据
     * @return 方法答案
     */
    public List<Method> getMethodAnswer(String[] searchMsg) {
        Method method = new Method();
        if (StringUtils.isNotEmpty(searchMsg[1]) && !searchMsg[1].contains(OTHER_TYPE)) {
            method.setType(searchMsg[1]);
        }
        String searchNameAndKeyword = searchMsg[0];
        if (StringUtils.isNotEmpty(searchNameAndKeyword)) {
            String[] nameAndKewWord = searchNameAndKeyword.split("_");
            String name = nameAndKewWord[0];
            String keyWordArrStr;
            method.setName(name);
            if (nameAndKewWord.length == 1) {
                keyWordArrStr = nameAndKewWord[0];
            } else {
                keyWordArrStr = nameAndKewWord[1];
            }
            keyWordArrStr = keyWordArrStr.replaceAll("，", ",");
            method.setKeyWord(keyWordArrStr);
            logger.info("通过 [" + method.toString() + "] 开始查找信息 ");
            List<Method> methodList = methodDao.findByEntity(method);
            if (null == methodList || methodList.isEmpty()) {
                logger.info("通过 [" + method.toString() + "] 没有查到信息");
                method.setKeyWord(null);
                logger.info("通过 [" + method.toString() + "] 开始查找信息 ");
                methodList = methodDao.findByEntity(method);
                if (null == methodList || methodList.isEmpty()) {
                    logger.info("通过 [" + method.toString() + "] 没有查到信息");
                    method.setKeyWord(keyWordArrStr);
                    method.setName(null);
                    logger.info("通过 [" + method.toString() + "] 开始查找信息 ");
                    methodList = methodDao.findByEntity(method);
                    if (null == methodList || methodList.isEmpty()) {
                        logger.info("通过 [" + method.toString() + "] 没有查到信息");
                        method.setType(null);
                        method.setName(name);
                        logger.info("通过 [" + method.toString() + "] 开始查找信息 ");
                        methodList = methodDao.findByEntity(method);
                        if (null == methodList || methodList.isEmpty()) {
                            logger.info("通过 [" + method.toString() + "] 没有查到信息");
                            method.setKeyWord(null);
                            logger.info("通过 [" + method.toString() + "] 开始查找信息 ");
                            methodList = methodDao.findByEntity(method);
                            if (null == methodList || methodList.isEmpty()) {

                                String[] keyWordList = keyWordArrStr.split(",");
                                methodList = new ArrayList<>();
                                if (keyWordList.length > 0) {
                                    List<Method> methods = methodDao.findByKey(Arrays.asList(keyWordList));
                                    if (null != methods && !methods.isEmpty()) {
                                        methodList.addAll(methods);
                                        logger.info("通过 [" + Arrays.toString(keyWordList) + "] 查到信息 ");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!methodList.isEmpty()) {
                return methodList;
            }
        }
        return null;
    }

    /**
     * 导入方法
     *
     * @param absolutePath 绝对路径
     * @return 导入成功与否的信息
     */
    public String importMethod(String absolutePath) {
        StringBuilder returnMsg = new StringBuilder();
        //TODO:fix
        List<Method> methods = ReadFile.readMethodMsg(absolutePath);
        if (null != methods && !methods.isEmpty()) {
            //成功导入的数量
            int successNum = 0;
            int fileNum = 0;
            for (Method method : methods) {
                List<Method> methodMsgList = getMethodByName(method.getName());
                if (null == methodMsgList || methodMsgList.isEmpty()) {
                    if (!methodDao.save(method)) {
                        logger.info("导入 [" + method.toString() + "] 到数据库失败 ");
                        returnMsg.append("导入 [").append(method.toString()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    } else {
                        successNum++;
                        logger.info("导入 [" + method.toString() + "] 到数据库成功 ");
                    }
                } else {
                    method.setId(methodMsgList.get(0).getId());
                    if (!methodDao.update(method)) {
                        logger.info("更新 [" + method.toString() + "] 到数据库失败 ");
                        returnMsg.append("更新 [").append(method.toString()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                    }
                }
            }
            returnMsg.append(Constants.LINE_SEPARATOR).append(" 成功导入 [").append(successNum).append("] 个文件").append(Constants.LINE_SEPARATOR);
            returnMsg.append("失败导入 [").append(fileNum).append("] 个文件").append(Constants.LINE_SEPARATOR);
            logger.info("成功导入 [" + successNum + "] 个文件到数据库成功 ");
            logger.info("失败导入 [" + fileNum + "] 个文件到数据库成功 ");
        } else {
            logger.info("导入文件 [" + absolutePath + "] 失败 ");
            returnMsg.append("导入文件失败").append(Constants.LINE_SEPARATOR);
        }
        return returnMsg.toString();
    }

    /**
     * 通过名字查询，避免重复数据
     *
     * @param name 方法名
     * @return
     */
    private List<Method> getMethodByName(String name) {
        return methodDao.findByName(name);
    }

    /**
     * 添加方法
     *
     * @param name        方法名
     * @param type        类型
     * @param language    语言
     * @param keyWord     关键字
     * @param answer      答案
     * @param description 描述
     * @return 成功与否
     */
    public boolean addMethod(String name, String type, String language, String keyWord, String answer, String description) {
        logger.info("进入添加方法");
        Method method = new Method();
        method.setId(GenerateRandom.randomInt());
        method.setName(name);
        if (StringUtils.isNotEmpty(type)) {
            method.setType(type);
        }
        if (StringUtils.isNotEmpty(language)) {
            method.setLanguage(language);
        }
        method.setKeyWord(keyWord);
        method.setAnswer(answer);
        method.setDescription(description);
        method.setUpdateDate(new Date());
        return methodDao.save(method);
    }

    /**
     * 获取所有的方法
     *
     * @return
     */
    public String[] getAllMethods() {
        logger.info("查询所有的 Method 数据 ");
        List<Method> methodList = methodDao.findAll();
        StringBuilder methodStr = new StringBuilder();
        String line = System.getProperty("line.separator");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，使用次数，方法描述，是否过时
        for (Method method : methodList) {
            methodStr.append(method.getId())
                    .append("，").append(method.getName().replaceAll("，", ","))
                    .append("，");
            if (StringUtils.isNotEmpty(method.getLanguage())) {
                methodStr.append(method.getLanguage().replaceAll("，", ","));
            }
            methodStr.append("，");
            if (StringUtils.isNotEmpty(method.getType())) {
                methodStr.append(method.getType().replaceAll("，", ","));
            }
            methodStr.append("，").append(method.getAnswer().replaceAll("，", ",")).append("，")
                    .append(method.getKeyWord().replaceAll("，", ",")).append("，")

                    .append(simpleDateFormat.format(method.getUpdateDate()))
                    .append("，使用").append(method.getUseTimes()).append("次，");
            if (StringUtils.isNotEmpty(method.getDescription())) {
                methodStr.append(method.getDescription().replaceAll("，", ","));
            }
            methodStr.append("，")
                    .append(method.getIsOutOfDate() == 0 ? "未过时" : "已过时").append(line);
        }
        logger.info("查询的 Method 数据 [" + methodList.size() + "] ");
        return methodStr.toString().split(line);
    }

    /**
     * 导出数据
     *
     * @param filePath
     * @param fileType
     * @return boolean
     */
    public boolean exportMethod(String filePath, String fileType) {
        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileType)) {
            return false;
        }
        String[] methodArr = getAllMethods();
        if (null != methodArr && methodArr.length > 0) {
            return ChooseWayFrame.exportChooseFile(filePath, fileType, methodArr, Method.class);
        }
        return false;
    }

    /**
     * 更新方法
     *
     * @param name
     * @param language
     * @param type
     * @param keyword
     * @param answer
     * @param description
     * @param mid
     * @return
     */
    public boolean updateMethod(String name, String language, String type, String keyword, String answer, String
            description, String mid) {
        logger.info("更新 Method 数据 ");
        Method method = new Method();
        method.setId(Integer.parseInt(mid));
        method.setName(name);
        method.setLanguage(language);
        method.setType(type);
        method.setKeyWord(keyword);
        method.setAnswer(answer);
        method.setDescription(description);

        return methodDao.update(method);
    }

    /**
     * 根据ID删除
     *
     * @param id
     * @return
     */
    public boolean deleteById(String id) {
        return methodDao.deleteById(Integer.parseInt(id));
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        methodDao.deleteAll();
    }
}
