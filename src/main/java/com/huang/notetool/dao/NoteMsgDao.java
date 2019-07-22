package com.huang.notetool.dao;

import com.huang.notetool.po.NoteMsg;

import java.util.List;

/**
 * NoteMsgDao
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明接口的解释
 * @date 2019-06-28 13:49:55
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-28   创建项目           完成
 */
public interface NoteMsgDao extends BaseDao<NoteMsg, Integer> {
    /**
     * 计数数据库数据量
     *
     * @return 数据库数据量
     */
    int countNum();

    /**
     * 查询所有的笔记名称
     *
     * @return 笔记名称数组
     */
    List<String> findAllName();

    /**
     * 通过笔记名称查询所有的笔记名称
     *
     * @param noteType 笔记名称
     * @return 笔记名称数组
     */
    List<String> findAllNameByType(String noteType);
}
