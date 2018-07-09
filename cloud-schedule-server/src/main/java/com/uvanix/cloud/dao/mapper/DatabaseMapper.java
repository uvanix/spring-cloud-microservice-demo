package com.uvanix.cloud.dao.mapper;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * 获取数据源
 *
 * @author uvanix
 * @date 2018/4/19
 */
public interface DatabaseMapper {

    String sql = "select database()";

    /**
     * 获取数据源
     *
     * @return
     */
    @Select(sql)
    @ResultType(String.class)
    String getDatabaseName();
}
