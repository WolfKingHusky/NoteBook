package com.huang.notetool.dao;

import com.huang.notetool.po.LinuxCmd;

import java.util.List;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-19 11:23:14
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-19   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public interface LinuxCmdDao extends BaseDao<LinuxCmd, Integer> {
//    /**
//     * 通过部分属性查询Linux命令
//     *
//     * @param linuxCmd Linux命令部分属性
//     * @return Linux命令
//     */
//    List<LinuxCmd> findById(LinuxCmd linuxCmd);

//    /**
//     * 根据 id查询数据
//     *
//     * @param id 主键
//     * @return Linux命令
//     */
//    LinuxCmd findById(Integer id);

//    /**
//     * 保存信息
//     *
//     * @param linuxCmd Linux命令
//     * @return true or false
//     */
//    boolean save(LinuxCmd linuxCmd);

//    /**
//     * 查询所有的Linux命令
//     *
//     * @return Linux命令
//     */
//    List<LinuxCmd> findAll();

//    /**
//     * 清空数据
//     */
//    void deleteAll();

//    /**
//     * 更新数据
//     *
//     * @param linuxCmd Linux命令
//     * @return 成功与否
//     */
//    boolean update(LinuxCmd linuxCmd);

//    /**
//     * 根据ID删除Linux信息
//     *
//     * @param id id
//     * @return true or false
//     */
//    boolean deleteById(Integer id);

    /**
     * 查询所有的 LinuxCmd和Example信息
     *
     * @return Linux命令
     */
    List<LinuxCmd> findAllLinuxCmdAndExample();

    /**
     * 查询数据库条目数
     *
     * @return 条目数
     */
    int countNum();

    /**
     * 通过名字搜索Linux命令
     *
     * @param name Linux命令名字
     * @return Linux命令集合
     */
    List<LinuxCmd> getLinuxCmdByName(String name);
}
