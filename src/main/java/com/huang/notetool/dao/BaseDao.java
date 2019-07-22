package com.huang.notetool.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 通用数据访问对象接口
 *
 * @param <E> 实体类型
 * @param <K> ID字段类型
 * @author huangzhaorong
 */
public interface BaseDao<E, K extends Serializable> {

    /**
     * 新增
     *
     * @param entity 实体对象
     * @return 保存成功返回true 否则返回false
     */
    boolean save(E entity);

    /**
     * 删除
     *
     * @param id 标识字段
     * @return 删除成功返回true 否则返回false
     */
    boolean deleteById(K id);

    /**
     * 删除
     *
     * @param entity 标识字段
     * @return 删除成功返回true 否则返回false
     */
    boolean deleteByEntity(E entity);

    /**
     * 修改
     *
     * @param entity 实体对象
     */
    boolean update(E entity);

    /**
     * 根据ID查询
     *
     * @param id ID字段
     * @return 实体对象或null
     */
    E findById(K id);

    /**
     * 查询所有
     *
     * @return 返回所有实体对象
     */
    List<E> findAll();

    /**
     * 关键字查询
     *
     * @param keyNames 关键字名称列表
     * @param keyNum   关键字数量
     * @return 保存所有实体对象的列表容器
     */
    List<E> findByKeyName(List<String> keyNames, int keyNum);

    /**
     * 对象字查询
     *
     * @param entity 实体类
     * @return 所有实体对象的列表容器
     */
    List<E> findByEntity(E entity);

    /**
     * MySQL状态查询
     *
     * @return mysql连接状态
     */
    boolean returnMysqlState();

    /**
     * 通过关键字组合查询
     *
     * @param keyList 键字组合
     * @return E
     */
    List<E> findByKey(List<String> keyList);

    /**
     * 通过名字查询
     *
     * @param name 名字（主要的，不能重复的信息）
     * @return 列表
     */
    List<E> findByName(String name);

    /**
     * 清空数据
     */
    void deleteAll();
}
