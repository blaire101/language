package com.qunar.fresh.librarysystem.search.book;

import java.util.List;

import com.google.common.base.Preconditions;
import com.qunar.fresh.librarysystem.model.Book;

/**
 * 搜索图书后得到的结果，用于封装图书信息列表和搜索的结果数量 User: hang.gao Date: 14-3-31 Time: 上午10:55
 */
public class BookSearchResult {

    /**
     * 搜索到的结果总数
     */
    private long totalHints;

    /**
     * 返回的结果
     */
    private List<Book> books;

    /**
     * 第几页的数据
     */
    private int page;

    /**
     * 一页的大小
     */
    private int pageSize;

    public BookSearchResult(long totalHints, List<Book> books, int page, int pageSize) {
        Preconditions.checkNotNull(books);
        Preconditions.checkArgument(totalHints >= 0);
        Preconditions.checkArgument(page > 0);
        Preconditions.checkArgument(pageSize > 0);
        this.totalHints = totalHints;
        this.books = books;
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getTotalHints() {
        return totalHints;
    }

    public List<Book> getBooks() {
        return books;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "BookSearchResult [totalHints=" + totalHints + ", books=" + books + ", page=" + page + ", pageSize="
                + pageSize + "]";
    }
}
