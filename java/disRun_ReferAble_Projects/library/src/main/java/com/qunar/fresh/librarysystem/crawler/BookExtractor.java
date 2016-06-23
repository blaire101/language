package com.qunar.fresh.librarysystem.crawler;

import com.qunar.fresh.librarysystem.model.Book;

/**
 * @author hang.gao
 */
public interface BookExtractor {

    /**
     * 生成图书信息
     * 
     * @param bookName 图书名
     * @return 图书的信息
     */
    Book generate(String bookName);

}