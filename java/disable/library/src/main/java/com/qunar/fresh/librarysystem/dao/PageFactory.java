package com.qunar.fresh.librarysystem.dao;

/**
 * 返回Page对象的工厂
 * 
 * @author hang.gao
 */
public interface PageFactory {
    public Page newPage(int offset, int limit);
}
