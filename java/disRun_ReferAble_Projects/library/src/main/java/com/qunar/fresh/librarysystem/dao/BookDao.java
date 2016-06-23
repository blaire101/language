package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: jinglv Date: 14-3-27 Time: 下午6:34
 * 
 * @author jing.lv
 * @author hang.gao
 * @author libin.chen
 */
@Repository
public interface BookDao {

    public Set<Category> selectNav(@Param("title") String title);

    public int saveBookInfo(Book book);

    public int saveNav(@Param("title") String title, @Param("bookType") String bookType);

    public Integer selectBookNavId(@Param("title") String title, @Param("bookType") String bookType);

    public Book selectBookInfo(@Param("bookName") String bookName, @Param("bookAuthor") String bookAuthor,
            @Param("navId") int navId);

    public int saveBook(@Param("bookId") String bookId, @Param("bookInfoId") int bookInfoId,
            @Param("bookStatus") BookStatus bookStatus, @Param("bookLib") int libId);

    public int updateBookStatus(@Param("bookId") String bookId, @Param("bookStatus") BookStatus bookStatus);

    public int selectBookCount(@Param("bookInfoId") Integer bookInfoId, @Param("bookLib") Integer bookLib);

    public int selectBookCountByBookInfo(@Param("bookInfoId") Integer bookInfoId);

    public BookStatus selectBookStatusByBookId(@Param("bookId") String bookId);

    public int selectBookInfoIdByBookId(@Param("bookId") String bookId);

    /**
     * 查询书的可预约的数量
     * 
     * @param bookInfoId 书的信息的id
     * @param libId 图书馆的id
     * @return 图书馆中书的数量
     * @author hang.gao
     */
    public int selectBookReserveCountByBookInfoAndLibrary(@Param("book_info_id") int bookInfoId,
            @Param("lib_id") int libId);

    public int selectBorrowedCountByBookId(@Param("bookId") String bookId);

    public List<Book> selectBookList(RowBounds rowBounds, @Param("bookLib") int bookLib);

    public int selectBookCountByBookId(@Param("bookId") String bookId);

    /**
     * 通过书名查询图书，书名可能不只一个
     * 
     * @param names 书名的集合
     * @return 书的集合中，所有书名对应的书的信息
     * @author hang.gao
     */
    List<Book> selectBookByNames(List<String> names);

    /**
     * 按照类型查询
     * 
     * @param typeid 类型id
     * @param page 分页查询中的第几页
     * @return 查询到的数据
     * @author hang.gao
     */
    List<Book> selectByType(@Param("type") int typeid, RowBounds page);

    /**
     * 查询某一分类下借阅次数的top n的图书
     * 
     * @param typeid 图书的类型
     * @return 查询到的书的列表
     * @author hang.gao
     */
    List<Book> selectTop(@Param("type") int typeid, RowBounds page);

    /**
     * 计算某一类型的图书的数量
     * 
     * @param typeid 类型id
     * @return 图书的数量
     * @author hang.gao
     */
    long countBookByType(@Param("type") int typeid);

    /**
     * 更新图书热度的值
     * 
     * @param bookInfoId
     */
    void updateHot(@Param("book_info_id") int bookInfoId);

    /**
     * 通过图书的条码查询图书
     * 
     * @param bookId 图书的条码
     * @return 条码对应的图书信息，没有则返回null
     * @author hang.gao
     */
    Book selectBookByBookId(@Param("book_id") String bookId);

    /**
     * 根据 bookId ，查询出图书的可更改的详细信息
     * 
     * @author : libin.chen
     *         <p/>
     *         涉及表 ： (图书表 book，图书信息表 book_info，分类表 navigation)
     */
    public Map getBookByBookId(@Param("book_id") String bookId);

    /**
     * 根据 bookId ，查询出图书的 bookInfoId
     * 
     * @author : libin.chen
     *         <p/>
     *         涉及表 ： (图书表 book)
     */
    public Integer getBookInfoIdByBookId(@Param("book_id") String bookId);

    /**
     * 更新图书信息，如 ： 书名， 作者，出版社, 简介
     * 
     * @author : libin.chen
     *         <p/>
     *         涉及表 ： (图书信息表 book_info)
     */
    public int updateBookInfo(@Param("book_name") String bookName, @Param("author") String bookAuthor,
            @Param("press") String bookPress, @Param("intro") String bookIntro, @Param("nav_id") Integer navId,
            @Param("id") Integer bookInfoId);

    public void deleteBookOfLib(@Param("libId") int libId, @Param("status") BookStatus status);

    /**
     * 得到一个分类的热度
     * 
     * @param navId
     * @return
     */
    public Integer fetchHotByNavId(@Param("navId") int navId);
}
