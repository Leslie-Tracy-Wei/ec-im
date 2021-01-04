package ec.im.webServer.base.dao;


import ec.im.webServer.base.pagination.Pagination;

import java.util.List;

/**
 * DAO基础类，封装单表的insert/update/delete操作
 */
public interface BaseDao<T,PK> {
    int insert(T data);

    int insertSelective(T data);

    /**
     * 批量插入数据
     * @param datas 数据
     * @return 返回操作记录数
     */
    int insertBatch(List<T> datas);

    /**
     * 更新数据
     * 主键为更新条件，其他为数据
     * @param data 数据
     * @return 更新结果行数
     */
    int update(T data);

    /**
     * 更新数据，忽略空字段
     * 主键为更新条件，其他非空字段为数据
     * @param data 数据
     * @return 更新结果行数
     */
    int updateSelective(T data);

    /**
     * 通过主键删除记录
     * @param ids  主键
     * @return    删除行数
     */
    int delete(PK... ids);

    /**
     * 通过主键使记录无效（相当于逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    int disable(PK... ids);

    /**
     * 通过主键使记录有效（相当于恢复逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    int enable(PK... ids);

    /**
     * 通过主键获取数据
     * @param id  主键
     * @return    一行数据
     */
    T get(PK id);

    /**
     * 通过主键获取数据
     * @param ids  主键
     * @return List 如果无数据时，返回是长度为0的List对象
     */
    List<T> getByIds(PK... ids);

    /**
     * 通过Model获取数据
     * @param data  Model数据，非空字段都做为条件查询
     * @return    数据列表
     */
    List<T> selectAll(T data);

    /**
     * 通过pagination对象进行相关参数查询，获取分页数据
     * @param pagination
     * @return    数据列表
     */
    List<T> search(Pagination<T> pagination);

}
