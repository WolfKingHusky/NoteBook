package com.huang.notetool.dao.impl;

import com.huang.notetool.dao.LinuxCmdDao;
import com.huang.notetool.po.LinuxCmd;
import com.huang.notetool.po.LinuxCmdExam;
import com.huang.notetool.util.DBUtil;
import com.huang.notetool.util.GenerateRandom;
import com.huang.notetool.util.UtilSql;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-19 11:23:27
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-19   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxCmdDaoImpl implements LinuxCmdDao {
    /**
     * 日志初始化
     */
    private Logger logger = Logger.getLogger(LinuxCmdDaoImpl.class);

    /**
     * 通过部分属性查询Linux命令
     *
     * @param linuxCmd Linux命令部分属性
     * @return Linux命令
     */
    @Override
    public List<LinuxCmd> findByEntity(LinuxCmd linuxCmd) {
        return null;
    }

    /**
     * MySQL状态查询
     *
     * @return mysql连接状态
     */
    @Override
    public boolean returnMysqlState() {
        return false;
    }

    /**
     * 通过关键字组合查询
     *
     * @param keyList 键字组合
     * @return E
     */
    @Override
    public List<LinuxCmd> findByKey(List<String> keyList) {
        return null;
    }

    /**
     * 通过名字查询
     *
     * @param name 名字（主要的，不能重复的信息）
     * @return 列表
     */
    @Override
    public List<LinuxCmd> findByName(String name) {
        return null;
    }

    /**
     * 根据 id查询数据
     *
     * @param id 主键
     * @return Linux命令
     */
    @Override
    public LinuxCmd findById(Integer id) {
        List<LinuxCmd> linuxCmdList = DBUtil.connectionLinuxCmd(UtilSql.findLinuxCmdMsgByID(id));
        if (null != linuxCmdList && !linuxCmdList.isEmpty()) {
            LinuxCmd linuxCmd = linuxCmdList.get(0);
            if (null != linuxCmd.getLinuxCmdExam()) {
                LinuxCmdExam linuxCmdExam = getLinuxCmdExamById(linuxCmd.getLinuxCmdExam().getId());
                if (null != linuxCmdExam) {
                    linuxCmd.setLinuxCmdExam(linuxCmdExam);
                }
            }
            return linuxCmd;
        }
        return null;
    }

    /**
     * 保存信息
     *
     * @param linuxCmd Linux命令
     * @return true or false
     */
    @Override
    public boolean save(LinuxCmd linuxCmd) {
        linuxCmd.setId(GenerateRandom.randomInt());
        boolean result;
        if (null != linuxCmd.getLinuxCmdExam()) {
            linuxCmd.getLinuxCmdExam().setId(linuxCmd.getId());
            result = saveLinuxCmdExam(linuxCmd.getLinuxCmdExam());
            if (!result) {
                //添加失败，删除关联表信息
                logger.warn("添加Linux命令信息失败，清除关联表对应[id=" + linuxCmd.getId() + "]信息");
                //                deleteById(linuxCmd.getId());
            } else {
                result = DBUtil.connection(UtilSql.addByLinuxCmd(linuxCmd));
            }
        } else {
            result = DBUtil.connection(UtilSql.addByLinuxCmd(linuxCmd));
        }
        return result;
    }

    /**
     * 查询所有的Linux命令
     *
     * @return Linux命令
     */
    @Override
    public List<LinuxCmd> findAll() {
        return null;
    }

    /**
     * 关键字查询
     *
     * @param keyNames
     * @param keyNum
     * @return 保存所有实体对象的列表容器
     */
    @Override
    public List<LinuxCmd> findByKeyName(List<String> keyNames, int keyNum) {
        return null;
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        DBUtil.connection(UtilSql.delAllLinuxCmdExam());
        DBUtil.connection(UtilSql.delAllLinuxCmd());
    }

    /**
     * 更新数据
     *
     * @param linuxCmd Linux命令
     * @return 成功与否
     */
    @Override
    public boolean update(LinuxCmd linuxCmd) {
        boolean result = false;
        if (null != linuxCmd.getLinuxCmdExam()) {
            if (null == linuxCmd.getLinuxCmdExam().getId()) {
                linuxCmd.getLinuxCmdExam().setId(linuxCmd.getId());
                result = saveLinuxCmdExam(linuxCmd.getLinuxCmdExam());
            } else {
                result = DBUtil.connection(UtilSql.updateLinuxCmdExamMsgSql(linuxCmd.getLinuxCmdExam()));
            }
        }
        if (result) {
            DBUtil.connection(UtilSql.updateLinuxCmdMsgSql(linuxCmd));
        }
        return result;
    }

    /**
     * 保存LinuxCmdExam信息
     *
     * @param linuxCmdExam Linux参数说明关联表
     * @return true or false
     */
    private boolean saveLinuxCmdExam(LinuxCmdExam linuxCmdExam) {
        return DBUtil.connection(UtilSql.addByLinuxCmdExam(linuxCmdExam));
    }

    /**
     * 根据ID删除Linux信息
     *
     * @param id id
     * @return true or false
     */
    @Override
    public boolean deleteById(Integer id) {
        boolean result;
        LinuxCmd linuxCmd = findById(id);
        if (null != linuxCmd.getLinuxCmdExam()) {
            Integer linuxCmdExamId = linuxCmd.getLinuxCmdExam().getId();
            result = DBUtil.connection(UtilSql.delLinuxCmdExamMsgById(linuxCmdExamId));
        } else {
            result = true;
        }
        if (result) {
            //删除信息
            return DBUtil.connection(UtilSql.delLinuxCmdMsgById(id));
        } else {
            return false;
        }
    }

    /**
     * 删除
     *
     * @param entity 标识字段
     * @return 删除成功返回true 否则返回false
     */
    @Override
    public boolean deleteByEntity(LinuxCmd entity) {
        return false;
    }

    /**
     * 查询所有的 LinuxCmd和Example信息
     *
     * @return Linux命令
     */
    @Override
    public List<LinuxCmd> findAllLinuxCmdAndExample() {
        logger.info("查询所有的 LinuxCmd和Example信息");
        List<LinuxCmd> linuxCmdList = DBUtil.connectionLinuxCmd(UtilSql.findAllLinuxCmdMsg());
        if (null != linuxCmdList && !linuxCmdList.isEmpty()) {
            for (LinuxCmd linuxCmd : linuxCmdList) {
                if (null != linuxCmd && null != linuxCmd.getLinuxCmdExam()) {
                    Integer id = linuxCmd.getLinuxCmdExam().getId();
                    LinuxCmdExam linuxCmdExam = getLinuxCmdExamById(id);
                    if (null != linuxCmdExam) {
                        linuxCmd.setLinuxCmdExam(linuxCmdExam);
                    }
                }
            }
            return linuxCmdList;
        }
        return null;
    }

    /**
     * 根据ID获取LinuxCmdExam
     *
     * @param id ID
     * @return LinuxCmdExam
     */
    private LinuxCmdExam getLinuxCmdExamById(Integer id) {
        return DBUtil.connectionLinuxCmdExam(UtilSql.getLinuxCmdExamById(id));
    }

    /**
     * 查询数据库条目数
     *
     * @return 条目数
     */
    @Override
    public int countNum() {
        return DBUtil.connectCountTableNum(UtilSql.countLinuxCmdNum());
    }

    /**
     * 通过名字搜索Linux命令
     *
     * @param name Linux命令名字
     * @return Linux命令集合
     */
    @Override
    public List<LinuxCmd> getLinuxCmdByName(String name) {
        return DBUtil.connectionLinuxCmd(UtilSql.findAllLinuxCmdMsgByName(name));
    }
}
