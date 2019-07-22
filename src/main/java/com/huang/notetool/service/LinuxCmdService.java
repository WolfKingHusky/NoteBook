package com.huang.notetool.service;

import com.huang.notetool.dao.LinuxCmdDao;
import com.huang.notetool.dao.impl.LinuxCmdDaoImpl;
import com.huang.notetool.po.LinuxCmd;
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
 * Linux命令service
 *
 * @author huang
 * @date 2019-06-19
 */
public class LinuxCmdService {
    /**
     * Dao层
     */
    private LinuxCmdDao linuxCmdDao;
    /**
     * 初始化日志
     */
    private static Logger logger = Logger.getLogger(LinuxCmdService.class);

    public LinuxCmdService() {
        linuxCmdDao = new LinuxCmdDaoImpl();
    }

    /**
     * 获取Linux命令信息
     * "Linux命令", "作用", "用法", "使用权限"
     *
     * @param linuxCmd 查询信息
     * @return 查找到的信息
     */
    public List<LinuxCmd> getLinuxCmd(LinuxCmd linuxCmd) {
        return linuxCmdDao.findByEntity(linuxCmd);
    }


    /**
     * 查询数据
     *
     * @param id 表主键
     */
    private LinuxCmd getLinuxCmd(Integer id) {
        return linuxCmdDao.findById(id);
    }


    /**
     * 新增LinuxCmd信息
     * 不支持
     *
     * @param linuxCmd Linux命令信息
     * @return true or false
     */
    public boolean addLinuxCmd(LinuxCmd linuxCmd) {
        logger.info("开始保存 linuxCmd 对象 ");
        return linuxCmdDao.save(linuxCmd);
    }

    /**
     * 查询所有的 LinuxCmd信息
     *
     * @return LinuxCmd信息
     */
    public List<LinuxCmd> getAll() {
        logger.info("查询所有的 LinuxCmd 数据 ");
        return linuxCmdDao.findAll();
    }

    /**
     * 查询所有的 LinuxCmd信息
     *
     * @param beginRow 开始行
     * @param endRow   结算行
     * @return LinuxCmd信息
     */
    public String[] getAllReturnArr(int beginRow, int endRow) {
        List<LinuxCmd> linuxCmdList = getAll();
        //返回列表
        List<String> returnLinuxCmdList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //主键编号，Linux命令，作用，用法，使用权限，更新时间，更新次数
        for (LinuxCmd linuxCmd : linuxCmdList) {
            stringBuilder.append(linuxCmd.getId());
            stringBuilder.append("，").append(linuxCmd.getLinuxCmd().replaceAll("[\n\t\r]", ","));
            stringBuilder.append("，");
            if (StringUtils.isNotEmpty(linuxCmd.getUsage())) {
                stringBuilder.append(linuxCmd.getUsage().replaceAll("[\n\t\r]", ",").replaceAll("，", ","));
            }
            stringBuilder.append("，");
            //用法
            if (StringUtils.isNotEmpty(linuxCmd.getExample())) {
                stringBuilder.append(linuxCmd.getExample().replaceAll("[\n\t\r]", ",").replaceAll("，", ","));
            }
            stringBuilder.append("，");
            //使用权限
            if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
                stringBuilder.append(linuxCmd.getPermission().replaceAll("[\n\t\r]", ",").replaceAll("，", ","));
            }
            //更新时间
            stringBuilder.append("，");
            if (null != (linuxCmd.getUpdateDate())) {
                stringBuilder.append(simpleDateFormat.format(linuxCmd.getUpdateDate()));
            } else {
                stringBuilder.append(simpleDateFormat.format(new Date()));
            }
            stringBuilder.append("，");
            //更新次数
            stringBuilder.append("更新").append(linuxCmd.getUpdateTimes()).append("次");
            returnLinuxCmdList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        if (!returnLinuxCmdList.isEmpty()) {
            return returnLinuxCmdList.toArray(new String[0]);
        }
        return null;
    }

    /**
     * 查询所有的 LinuxCmd和Example信息
     *
     * @return LinuxCmd信息
     */
    public List<LinuxCmd> getAllLinuxCmdAndExample() {
        logger.info("查询所有的 LinuxCmd 数据 ");
        return linuxCmdDao.findAllLinuxCmdAndExample();
    }

    /**
     * 查询所有的 LinuxCmd和Example信息
     *
     * @param beginRow 开始行
     * @param endRow   结算行 ,结算行为0表示查询所有的数据
     * @return LinuxCmd信息
     */
    public String[] getAllLinuxCmdAndExampleReturnArr(int beginRow, int endRow) {
        List<LinuxCmd> linuxCmdList = getAllLinuxCmdAndExample();
        //返回列表
        List<String> returnLinuxCmdList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null == linuxCmdList) {
            logger.info("Linux数据为空");
            return null;
        }
        //主键编号，Linux命令，作用，用法，使用权限，更新时间，更新次数,参数说明,多个实例(使用中文；作为换行)，添加时间
        for (LinuxCmd linuxCmd : linuxCmdList) {
            stringBuilder.append(linuxCmd.getId());
            stringBuilder.append("，").append(linuxCmd.getLinuxCmd().replaceAll("[\n\t\r]", "").replaceAll("，", ","));
            stringBuilder.append("，");
            if (StringUtils.isNotEmpty(linuxCmd.getUsage())) {
                stringBuilder.append(linuxCmd.getUsage().replaceAll("，", ",").replaceAll("[\n\t\r]", ""));
            }
            stringBuilder.append("，");
            //用法
            if (StringUtils.isNotEmpty(linuxCmd.getExample())) {
                stringBuilder.append(linuxCmd.getExample().replaceAll("，", ",").replaceAll("[\n\t\r]", ""));
            }
            stringBuilder.append("，");
            //使用权限
            if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
                stringBuilder.append(linuxCmd.getPermission().replaceAll("，", ","));
            }
            //更新时间
            stringBuilder.append("，");
            if (null != (linuxCmd.getUpdateDate())) {
                stringBuilder.append(simpleDateFormat.format(linuxCmd.getUpdateDate()));
            } else {
                stringBuilder.append(simpleDateFormat.format(new Date()));
            }
            stringBuilder.append("，");
            //更新次数
            stringBuilder.append("更新").append(linuxCmd.getUpdateTimes()).append("次");
            stringBuilder.append("，");
            if (null != linuxCmd.getLinuxCmdExam()) {
                if (null != linuxCmd.getLinuxCmdExam().getParameterNote()) {
                    stringBuilder.append(linuxCmd.getLinuxCmdExam().getParameterNote().replaceAll("，", ",").replaceAll("[\n\t\r]", "；"));
                }
                stringBuilder.append("，");
                if (null != linuxCmd.getLinuxCmdExam().getExamples()) {
                    stringBuilder.append(linuxCmd.getLinuxCmdExam().getExamples().replaceAll("，", ",").replaceAll("[\n\t\r]", "；"));
                }
                stringBuilder.append("，");
                if (null != (linuxCmd.getLinuxCmdExam().getInsertDate())) {
                    stringBuilder.append(simpleDateFormat.format(linuxCmd.getLinuxCmdExam().getInsertDate()));
                } else {
                    stringBuilder.append(simpleDateFormat.format(new Date()));
                }
            } else {
                //,参数说明,多个实例(使用中文；作为换行)，添加时间
                stringBuilder.append("，").append("，").append(simpleDateFormat.format(new Date()));
            }

            returnLinuxCmdList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        if (!returnLinuxCmdList.isEmpty()) {
            return returnLinuxCmdList.toArray(new String[0]);
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
        logger.info("进入导入页面,准备导入Linux命令数据");
        List<LinuxCmd> linuxCmdList = ReadFile.readLinuxCmdMsg(filePath);
        int successNum = 0;
        int fileNum = 0;
        if (null != linuxCmdList && !linuxCmdList.isEmpty()) {
            for (LinuxCmd linuxCmd : linuxCmdList) {
                List<LinuxCmd> linuxCmdListFromDB = getByLinuxCmd(linuxCmd.getLinuxCmd());
                if (null == linuxCmdListFromDB || linuxCmdListFromDB.isEmpty()) {
                    if (!addLinuxCmd(linuxCmd)) {
                        logger.info("导入 [" + linuxCmd.toString() + "] 到数据库失败 ");
                        stringBuilder.append("导入 [").append(linuxCmd.getLinuxCmd()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
                        fileNum++;
                    } else {
                        stringBuilder.append("导入 [").append(linuxCmd.getLinuxCmd()).append("] 成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    }
                } else {
                    linuxCmd.setId(linuxCmdListFromDB.get(0).getId());
                    if (linuxCmdDao.update(linuxCmd)) {
                        stringBuilder.append("更新 [").append(linuxCmd.getLinuxCmd()).append("] 到数据库成功 ").append(Constants.LINE_SEPARATOR);
                        successNum++;
                    } else {
                        logger.info("更新 [" + linuxCmd.getLinuxCmd() + "] 到数据库失败 ");
                        stringBuilder.append("更新 [").append(linuxCmd.getLinuxCmd()).append("] 到数据库失败 ").append(Constants.LINE_SEPARATOR);
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
     * 通过Linux名字查询信息
     *
     * @param name Linux名字
     * @return Linux命令列表
     */
    private List<LinuxCmd> getByLinuxCmd(String name) {
        return linuxCmdDao.getLinuxCmdByName(name);
    }

    /**
     * 导出数据
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @return 成功与否
     */
    public boolean exportLinuxCmdMsg(String filePath, String fileType) {
        logger.info("准备导出 LinuxCmd 信息.... ");
        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileType)) {
            logger.warn("导出 LinuxCmd 信息失败！ ");
            return false;
        }
        //Linux全量信息
        String[] linuxCmdArr = getAllLinuxExportMsg();
        if (null != linuxCmdArr && linuxCmdArr.length > 0) {
            return ChooseWayFrame.exportChooseFile(filePath, fileType, linuxCmdArr, LinuxCmd.class);
        }
        return false;
    }

    /**
     * 根据ID删除信息
     *
     * @param id id
     * @return 成功与否
     */
    public boolean deleteById(String id) {
        return linuxCmdDao.deleteById(Integer.parseInt(id));
    }

    /**
     * 更新信息
     *
     * @return true or false
     */
    public boolean update(LinuxCmd linuxCmd) {
        logger.info("更新 LinuxCmd 数据 ");
        return linuxCmdDao.update(linuxCmd);
    }


    /**
     * 清空数据
     */
    public void deleteAll() {
        linuxCmdDao.deleteAll();
    }

    /**
     * 查询数据库条目数
     *
     * @return 条目数
     */
    public int countSize() {
        return linuxCmdDao.countNum();
    }

    /**
     * 根据ID查询信息
     *
     * @param id ID
     * @return LinuxCmd
     */
    public LinuxCmd findMsgById(String id) {
        return linuxCmdDao.findById(Integer.parseInt(id));
    }

    /**
     * 根据Linux名称查询数据
     *
     * @param linuxCmd Linux命令名称
     * @return Linux命令
     */
    public List<LinuxCmd> findMsgByName(String linuxCmd) {
        return linuxCmdDao.getLinuxCmdByName(linuxCmd);
    }

    /**
     * 查询所有的 LinuxCmd和Example信息
     *
     * @return LinuxCmd信息
     */
    public String[] getAllLinuxExportMsg() {
        List<LinuxCmd> linuxCmdList = getAllLinuxCmdAndExample();
        //返回列表
        List<String> returnLinuxCmdList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null == linuxCmdList) {
            logger.info("Linux数据为空");
            return null;
        }
        //Linux命令，作用，用法，使用权限,参数说明(使用中文；作为换行),多个实例(使用中文；作为换行)
        for (LinuxCmd linuxCmd : linuxCmdList) {
            stringBuilder.append(linuxCmd.getLinuxCmd().replaceAll("[\n\t\r]", "").replaceAll("，", ","));
            stringBuilder.append("，");
            if (StringUtils.isNotEmpty(linuxCmd.getUsage())) {
                stringBuilder.append(linuxCmd.getUsage().replaceAll("，", ",").replaceAll("[\n\t\r]", ""));
            }
            stringBuilder.append("，");
            //用法
            if (StringUtils.isNotEmpty(linuxCmd.getExample())) {
                stringBuilder.append(linuxCmd.getExample().replaceAll("，", ",").replaceAll("[\n\t\r]", ""));
            }
            stringBuilder.append("，");
            //使用权限
            if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
                stringBuilder.append(linuxCmd.getPermission().replaceAll("，", ","));
            }
            stringBuilder.append("，");
            if (null != linuxCmd.getLinuxCmdExam()) {
                if (null != linuxCmd.getLinuxCmdExam().getParameterNote()) {
                    stringBuilder.append(linuxCmd.getLinuxCmdExam().getParameterNote().replaceAll("，", ",").replaceAll("[\n\t\r]", "；"));
                }
                stringBuilder.append("，");
                if (null != linuxCmd.getLinuxCmdExam().getExamples()) {
                    stringBuilder.append(linuxCmd.getLinuxCmdExam().getExamples().replaceAll("，", ",").replaceAll("[\n\t\r]", "；"));
                }
            } else {
                //,参数说明(使用中文；作为换行),多个实例(使用中文；作为换行)
                stringBuilder.append("，").append("，");
            }
            returnLinuxCmdList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        if (!returnLinuxCmdList.isEmpty()) {
            return returnLinuxCmdList.toArray(new String[0]);
        }
        return null;
    }
}
