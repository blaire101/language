package com.qunar.fresh.librarysystem.model;

import java.util.List;

/**
 * 图书的包装类，用于给图书列表增加图书的总数量信息
 * 
 * @author hang.gao
 * 
 */
public final class BookWrapper {

    /**
     * 图书列表
     */
    private List<Book> books;

    /**
     * 一共有多少图书
     */
    private long totalCount;

    /**
     * 未知数量
     */
    public static final int UNKNOWN = -1;

    public BookWrapper(List<Book> books, long totalCount) {
        super();
        this.books = books;
        if (totalCount >= 0) {
            this.totalCount = totalCount;
        } else {
            this.totalCount = UNKNOWN;
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public long getTotalCount() {
        return totalCount;
    }

}
