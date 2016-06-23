package com.qunar.fresh.librarysystem.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import com.qunar.fresh.librarysystem.search.book.BookSearchResult;

/**
 * @author jing.lv
 * @author hang.gao
 * @author libin.chen
 */
public interface BookService {

    /**
     * 添加新书，首先根据输入的书名和作者、所在图书馆查询该书具体信息是否已经存在，若存在，直接将该书的book_id和book_info_id插入book表
     * 若不存在，则首先将该书的具体信息添加到book_info表，然后查询该书的book_info_id，然后插入book表
     * 
     * @author jing.lv
     */
    public abstract Set<Category> addBookLoad(String title);

    public abstract String addBook(Book book, String userRtx);

    public abstract List<Book> getBookList(int limit, int offset, int bookLib);

    public abstract int getBookCountInOneLib(int bookLib);

    public abstract Map<String, Object> deleteBook(int limit, int offset, String bookId, int bookLib, String userRtx);

    public abstract int modifyBookStatus(String bookId, BookStatus bookStatus);

    /**
     * 通过关键词搜索图书，返回指定的一页的结果
     * 
     * @param keywords 搜索的关键词
     * @param page 页码，返回第几页的数据
     * @param pageCount 一页的大小
     * @return 搜索到的结果
     * @author hang.gao
     */
    BookSearchResult searchBook(String keywords, int page, int pageCount);

    /**
     * 通过图书名搜索图书
     * 
     * @param keywords
     * @param page
     * @param pageCount
     * @return
     */
    BookSearchResult searchBookByName(String keywords, int page, int pageCount);

    /**
     * 返回某一类型下借阅次数前n的书的列表
     * 
     * @param n 返回的书的数量
     * @param type 书的类型id
     * @return 借阅次为最多的n本书
     * @author hang.gao
     */
    List<Book> top(int n, int type);

    /**
     * 根据书的类别选择书
     * 
     * @param typeid 书的类别
     * @param page 分页查询中的第几页
     * @param pageSize 每一大的大小
     * @return 查询得到的书
     * @author hang.gao
     */
    BookWrapper bookOfType(int typeid, int page, int pageSize);

    /**
     * 通过书的条码查找书
     * 
     * @param bookId 图书的条码
     * @return 图书，如果没有则返回null
     * @author hang.gao
     */
    Book queryBookByBookId(String bookId);

    /**
     * 
     * @param bookInfoId
     */
    void updateBookHot(int bookInfoId);

    /**
     * 管理员修改图书页
     * <p/>
     * 根据 book 表的 book_id 连表查询出 这个本书的详细信息 (包括分类等)
     * 
     * @return 得到的可修改各个项 如 :bookName， bookPress， title 等
     * @author libin.chen
     */
    public Map getBookByBookId(String bookId);

    /**
     * 管理员修改图书页后，点击 “提交” 按钮, 检查更新更改后的表中字段
     * 
     * @author libin.chen
     */
    public int modifyBookInfo(Book book, String userRtx);

    /**
     * 图书的包装类，用于给图书列表增加图书的总数量信息
     * 
     * @author hang.gao
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

    /**
     * 删除一个图书馆的所有图书
     * 
     * @param libId
     * @return
     * @author: he.chen
     */
    public boolean deleteBookOfLib(int libId);

    /**
     * 得到一个分类的热度
     * 
     * @param navId
     * @return
     */
    public int fetchHotByNavId(int navId);
}
