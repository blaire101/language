package com.qunar.fresh.librarysystem.dao;

/**
 * 查询数据库中的一页
 * 
 * @author hang.gao
 */
public interface Page {

    public int getOffset();

    public int getLimit();
}
