package com.qunar.fresh.librarysystem.search;

/**
 * 可关闭的迭代器
 * 
 * @author hang.gao
 * 
 * @param <T> 迭代的元素的类型
 */
public interface Closeableiterable<T> extends Iterable<T> {

    /**
     * 关闭
     */
    void close();
}
