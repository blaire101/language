package com.qunar.fresh.librarysystem.search.book;

import com.qunar.fresh.librarysystem.dao.Page;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.search.SearchResult;

/**
 * 搜索引擎调用接口，用于搜索图书
 * <p/>
 * 
 * @author hang.gao Initial Created at 2014年3月27日
 *         <p/>
 */
public interface BookSearchEngine {

    /**
     * 搜索图书，keywords可以是书名，作者，图书类型等
     * 
     * @param keywords 图书关键词
     * @param start 返回结果的起始行
     * @param resultCount 最多返回的结果数，如果合法的结果不足resultCount则返回所有结果
     * @return 搜索到的结果，不为空
     */
    SearchResult searchBook(String keywords, int start, int resultCount);

    /**
     * 按照图书名搜索图书
     * 
     * @param keywords 搜索的关键字
     * @param page 搜索结果的某一页
     * @return 搜索结果
     */
    SearchResult searchBookByName(String keywords, Page page);

    /**
     * 创建索引
     * 
     * @throws java.io.IOException IO失败
     */
    void apendToIndex(Book book);

    /**
     * 更新图书的索引
     * 
     * @param book 新的图书数据
     */
    void updateIndex(Book book);

    /**
     * 删除图书的索引
     * 
     * @param bookInfoId 图书信息的ID
     */
    void deleteIndex(int bookInfoId);

    /**
     * 创建的索引的名字，对应于索引的数据的某个部分 //--------------------- Change Logs---------------------- //
     * <p/>
     * 
     * @author hang.gao Initial Created at 2014年3月27日
     *         <p/>
     *         //-------------------------------------------------------
     */
    public static enum IndexField {

        /**
         * book_info的主键
         */
        BOOK_INFO_ID,

        /**
         * 书名，表示对书名进行索引
         */
        BOOK_NAME,

        /**
         * 书名，表示对书名拼音进行索引
         */
        BOOK_NAME_PINYIN,

        /**
         * 书的作者
         */
        AUTHOR,

        /**
         * 书的作者名的拼音
         */
        AUTHOR_PINYIN,

        /**
         * 图书类型
         */
        BOOK_TYPE,

        /**
         * 图书类型的拼音
         */
        BOOK_TYPE_PINYIN,

        /**
         * 图书的简介
         */
        BOOK_INTRO,

        /**
         * 图书的简介的拼音
         */
        BOOK_INTRO_PINYIN,

    }

}
