package com.huang.notetool.service;

import com.huang.notetool.dao.NoteMsgDao;
import com.huang.notetool.dao.impl.NoteMsgDaoImpl;
import com.huang.notetool.po.NoteMsg;
import com.huang.notetool.tool.ChooseWayFrame;
import com.huang.notetool.tool.ReadFile;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 笔记service
 *
 * @author huang
 * @date 2019-06-28
 */
public class NoteMsgService {
    /**
     * Dao层
     */
    private NoteMsgDao noteMsgDao;
    /**
     * 初始化日志
     */
    private static Logger logger = Logger.getLogger(NoteMsgService.class);

    public NoteMsgService() {
        noteMsgDao = new NoteMsgDaoImpl();
    }

    /**
     * 获取笔记信息
     * "笔记信息", "作用", "用法", "使用权限"
     *
     * @param noteMsg 查询信息
     * @return 查找到的信息
     */
    public List<NoteMsg> getNoteMsg(NoteMsg noteMsg) {
        return noteMsgDao.findByEntity(noteMsg);
    }

    /**
     * 新增NoteMsg信息
     * 不支持
     *
     * @param noteMsg 笔记信息
     * @return true or false
     */
    public boolean addNoteMsg(NoteMsg noteMsg) {
        logger.info("开始保存 NoteMsg 对象 ");
        return noteMsgDao.save(noteMsg);
    }

    /**
     * 查询所有的 NoteMsg信息
     *
     * @return NoteMsg信息
     */
    public List<NoteMsg> getAll() {
        logger.info("查询所有的 NoteMsg 数据 ");
        return noteMsgDao.findAll();
    }

    /**
     * 查询所有的 NoteMsg信息
     *
     * @param beginRow 开始行
     * @param endRow   结算行
     * @return NoteMsg信息
     */
    public String[] findAll(int beginRow, int endRow) {
        List<NoteMsg> noteMsgList = getAll();
        //返回列表
        List<String> returnNoteMsgList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //笔记名称，笔记（BASE64编码），笔记类型，笔记说明，主键，更新方法时间，更新次数
        for (NoteMsg noteMsg : noteMsgList) {
            stringBuilder.append(noteMsg.getName().replaceAll("‘", "'"));
            stringBuilder.append("，").append(noteMsg.getNote());
            stringBuilder.append("，");
            //笔记类型
            if (StringUtils.isNotEmpty(noteMsg.getNoteType())) {
                stringBuilder.append(noteMsg.getNoteType().replaceAll("[\n\t\r]", ",").replaceAll("，", ","));
            }
            stringBuilder.append("，");
            //笔记说明
            if (StringUtils.isNotEmpty(noteMsg.getExplains())) {
                stringBuilder.append(noteMsg.getExplains().replaceAll("[\n\t\r]", ",").replaceAll("，", ","));
            }
            stringBuilder.append("，");
            //主键
            stringBuilder.append(noteMsg.getId());
            //更新时间
            stringBuilder.append("，");
            if (null != (noteMsg.getUpdateDate())) {
                stringBuilder.append(simpleDateFormat.format(noteMsg.getUpdateDate()));
            } else {
                stringBuilder.append(simpleDateFormat.format(new Date()));
            }
            stringBuilder.append("，");
            //更新次数
            stringBuilder.append("更新").append(noteMsg.getUpdateTimes()).append("次");
            returnNoteMsgList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        if (!returnNoteMsgList.isEmpty()) {
            return returnNoteMsgList.toArray(new String[0]);
        }
        return null;
    }

    /**
     * 导入数据
     *
     * @param filePath 文件绝对路径
     * @return 导入信息
     */
    public String importMethod(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        logger.info("进入导入页面,准备导入笔记信息数据... ");
        List<NoteMsg> noteMsgList = ReadFile.readNoteMsg(filePath);
        int successNum = 0;
        int fileNum = 0;
        if (null != noteMsgList && !noteMsgList.isEmpty()) {
            for (NoteMsg noteMsg : noteMsgList) {
                NoteMsg noteMsgFromDB = findMsgByName(noteMsg.getName());
                if (null == noteMsgFromDB) {
                    if (!addNoteMsg(noteMsg)) {
                        logger.info("导入 [" + noteMsg.toString() + "] 到数据库失败 ");
                        stringBuilder.append("导入 [").append(noteMsg.getName()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    } else {
                        stringBuilder.append("导入 [").append(noteMsg.getName()).append("] 成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    }
                } else {
                    noteMsg.setId(noteMsgFromDB.getId());
                    if (noteMsgDao.update(noteMsg)) {
                        stringBuilder.append("更新 [").append(noteMsg.getName()).append("] 到数据库成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    } else {
                        logger.info("更新 [" + noteMsg.getName() + "] 到数据库失败 ");
                        stringBuilder.append("更新 [").append(noteMsg.getName()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    }
                }
            }
        } else {
            logger.info("导入文件 [" + filePath + "] 失败 ");
            stringBuilder.append("导入文件失败").append(Constants.LINE_SEPARATOR);
        }
        stringBuilder.append(Constants.LINE_SEPARATOR).append("成功导入 [").append(successNum).append("] 个文件")
                .append(Constants.LINE_SEPARATOR);
        stringBuilder.append("失败导入 [").append(fileNum).append("] 个文件").append(Constants.LINE_SEPARATOR);
        return stringBuilder.toString();
    }

    /**
     * 导出数据
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @return 成功与否
     */
    public boolean exportNoteMsg(String filePath, String fileType) {
        logger.info("准备导出 NoteMsg 信息.... ");
        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileType)) {
            logger.warn("导出 NoteMsg 信息失败！ ");
            return false;
        }
        //笔记全量信息
        String[] noteMsgArr = findAll(0, 0);
        if (null != noteMsgArr && noteMsgArr.length > 0) {
            return ChooseWayFrame.exportChooseFile(filePath, fileType, noteMsgArr, NoteMsg.class);
        }
        return false;
    }

    /**
     * 查询所有的笔记名称
     *
     * @return 笔记名称数组
     */
    public String[] findAllName() {
        List<String> nameList = noteMsgDao.findAllName();
        if (null != nameList && !nameList.isEmpty()) {
            return nameList.toArray(new String[0]);
        }
        return null;
    }

    /**
     * 查询所有的笔记名称
     *
     * @return 笔记名称数组
     */
    public List<String> findAllNameList() {
        return noteMsgDao.findAllName();
    }

    /**
     * 根据ID删除信息
     *
     * @param id id
     * @return 成功与否
     */
    public boolean deleteById(String id) {
        return noteMsgDao.deleteById(Integer.parseInt(id));
    }

    /**
     * 更新信息
     *
     * @param noteMsg 笔记信息
     * @return true or false
     */
    public boolean update(NoteMsg noteMsg) {
        logger.info("更新 NoteMsg 数据 ");
        return noteMsgDao.update(noteMsg);
    }


    /**
     * 清空数据
     */
    public void deleteAll() {
        noteMsgDao.deleteAll();
    }

    /**
     * 查询数据库条目数
     *
     * @return 条目数
     */
    public int countSize() {
        return noteMsgDao.countNum();
    }

    /**
     * 根据ID查询信息
     *
     * @param id ID
     * @return NoteMsg
     */
    public NoteMsg findMsgById(String id) {
        return noteMsgDao.findById(Integer.parseInt(id));
    }

    /**
     * 根据笔记名称查询数据
     *
     * @param name 笔记信息名称
     * @return 笔记信息
     */
    public NoteMsg findMsgByName(String name) {
        List<NoteMsg> noteMsgList = noteMsgDao.findByName(name);
        if (null == noteMsgList || noteMsgList.isEmpty()) {
            return null;
        }
        return noteMsgList.get(0);
    }

    /**
     * 根据类型查询名字列表
     *
     * @param noteType 笔记类型
     * @return 名称列表
     */
    public List<String> findAllNameListByType(String noteType) {
        return noteMsgDao.findAllNameByType(noteType);
    }
}
