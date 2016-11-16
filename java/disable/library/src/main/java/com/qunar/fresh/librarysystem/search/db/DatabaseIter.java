package com.qunar.fresh.librarysystem.search.db;

import org.springframework.jdbc.core.RowMapper;

import com.qunar.fresh.librarysystem.search.Closeableiterable;

/**
 * 从数据库选择数据，进行迭代
 * 
 * @author hang.gao
 * 
 */
public interface DatabaseIter {

    /**
     * 查询
     * 
     * @param sql 查询的语句
     * @param mapper 每一行到对象的映射
     * @param objects SQL中的参数
     * @return 查询到的结果的迭代器
     */
    public <T> Closeableiterable<T> select(String sql, RowMapper<T> mapper, Object... objects);
}
