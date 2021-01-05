package ec.im.webServer.base.service;

import ec.im.webServer.base.exception.ApplicationException;
import ec.im.webServer.base.exception.ResponseCode;
import ec.im.webServer.base.mapper.GenericMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service基础类，封装单表的insert/update/delete操作
 *
 * @author liheng
 * @since 1.0
 */

@Transactional
public abstract class GenericService<T, PK> {
    protected static final Logger logger = LoggerFactory.getLogger(GenericService.class);
    protected GenericMapper<T, PK> genericMapper;

    public GenericService(GenericMapper<T, PK> genericMapper) {
        this.genericMapper = genericMapper;
    }

    /**
     * 插入数据
     *
     * 如果主键是基于DB的方式，数据插入成功后，主键值会自动填充到输入对象中
     *
     * @param data 数据
     * @return 返回操作记录数
     */
    public int insert(T data) {
        int result = 0;
        try {
//            setDefault(data, true);
            result = genericMapper.insert(data);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_EXCEPTION);
        }

        return result;
    }

    /**
     * 插入数据，忽略值为null的字段
     * @param data 数据
     * @return 返回操作记录数
     */
    public int insertSelective(T data) {
        int result = 0;
        try {
//            setDefault(data, true);
            result = genericMapper.insertSelective(data);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_EXCEPTION);
        }

        return result;
    }

    /**
     * 批量插入数据
     * @param datas 数据
     * @return 返回操作记录数
     */
    public int insertBatch(List<T> datas) {
        int result = 0;
        try {
            if (datas != null) {
//                for (T data : datas) {
////                    setDefault(data, true);
//                }
            }
            result = genericMapper.insertBatch(datas);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_BATCH_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_BATCH_EXCEPTION);
        }

        return result;
    }

    /**
     * 更新数据
     * 主键为更新条件，其他为数据
     * @param datas 数据
     * @return 更新结果行数
     */
    public int update(T... datas) {
        int result = 0;
        if (datas != null) {
            try {
                for (T data : datas) {
//                    setDefault(data, false);
                    result += genericMapper.update(data);
                }
            } catch (Exception e) {
                logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }

        return result;
    }

    /**
     * 更新数据，忽略空字段
     * 主键为更新条件，其他非null字段为数据
     * @param datas 数据
     * @return 更新结果行数
     */
    public int updateSelective(T... datas) {
        int result = 0;
        if (datas != null) {
            try {
                for (T data : datas) {
//                    setDefault(data, false);
                    result += genericMapper.updateSelective(data);
                }
            } catch (Exception e) {
                logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }

        return result;
    }

    /**
     * 通过主键删除记录
     * @param ids  主键
     * @return    删除行数
     */
    public int delete(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.delete(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.DELETE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.DELETE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键使记录无效（相当于逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    public int disable(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.disable(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.DELETE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.DELETE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键使记录有效（相当于恢复逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    public int enable(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.enable(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键获取数据
     * @param id  主键
     * @return    一行数据
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public T get(PK id) {
        T result = null;
        try {
            result = genericMapper.get(id);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_ONE_EXCEPTION.getMessage(), e);
        }

        return result;
    }



    /**
     * 通过主键获取数据
     * @param ids  主键
     * @return List 如果无数据时，返回是长度为0的List对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> getByIds(PK... ids) {
        List<T> result = null;
        try {
            result = genericMapper.getByIds(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_ONE_EXCEPTION.getMessage(), e);
        }
        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }

    /**
     * 通过Model获取数据
     * @param data  Model数据，非null字段都做为条件查询
     * @return List 如果无数据时，返回是长度为0的List对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> selectAll(T data) {
        List<T> result = null;
        try {
            result = genericMapper.selectAll(data);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_EXCEPTION.getMessage(), e);
        }

        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }


}