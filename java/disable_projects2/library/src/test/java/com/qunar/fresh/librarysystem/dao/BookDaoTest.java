package com.qunar.fresh.librarysystem.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created with IntelliJ IDEA. User: jinglv Date: 14-3-31 Time: 下午12:37
 *
 * @author hang.gao
 * @author jing.lv
 * @author libin.chen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class BookDaoTest {
    @Resource
    private BookDao bookDao;

    @Resource
    private PageFactory pageFactory;

    @Test
    @Transactional
    public void tsetSelectBookNavId() {
        bookDao.saveNav("计算机", "编程");
        Assert.assertTrue(bookDao.selectBookNavId("计算机", "编程") != 0);
    }

    @Test
    @Transactional
    public void testSaveBookInfo() {
        Book book = new Book();
        book.setBookName("C++Primer");
        book.setBookAuthor("roy");
        book.setBookPress("清华出版社");
        book.setBookIntro("C++ introduction");
        book.setImageURL("http://www.c++.jpg");
        book.setBookHot(0);
        book.setNavID(1);
        Assert.assertEquals(bookDao.saveBookInfo(book), 1);
    }

    @Test
    @Transactional
    public void testSelectBookInfoExistsBookInfo() {
        Book book = new Book();
        book.setBookName("java编程思想");
        book.setBookAuthor("jack");
        book.setBookPress("清华出版社");
        book.setBookIntro("java introduction");
        book.setImageURL("http://www.java.jpg");
        book.setBookHot(0);
        book.setNavID(1);
        bookDao.saveBookInfo(book);
        Assert.assertNotNull(bookDao.selectBookInfo("java编程思想", "jack", 1));
    }

    @Test
    @Transactional
    public void testSelectBookInfoNotExistsBookInfo() {
        Assert.assertNull(bookDao.selectBookInfo("java编程思想", "jack", 1));
    }

    @Test
    @Transactional
    public void testSaveBook() {
        Assert.assertEquals(
                bookDao.saveBook("qunar001", 1, BookStatus.INLIBRARY, 15), 1);
    }

    @Test
    @Transactional
    public void testUpdateBookStatusExistsBook() {
        bookDao.saveBook("qunar001", 1, BookStatus.INLIBRARY, 15);
        Assert.assertEquals(
                bookDao.updateBookStatus("qunar001", BookStatus.DELETED), 1);
    }

    @Test
    @Transactional
    public void testUpdateBookStatusNotExistsBook() {
        Assert.assertEquals(
                bookDao.updateBookStatus("qunar001", BookStatus.DELETED), 0);
    }

    @Test
    @Transactional
    public void testSelectBookList() {
        int count = bookDao.selectBookList(new RowBounds(0, 10), 15).size();
        Book book = new Book();
        book.setBookName("java编程思想");
        book.setBookAuthor("jack");
        book.setBookPress("清华出版社");
        book.setBookIntro("java introduction");
        book.setImageURL("http://www.java.jpg");
        book.setBookHot(0);
        book.setNavID(1);
        bookDao.saveBookInfo(book);
        int bookInfoID = bookDao.selectBookInfo("java编程思想", "jack", 1)
                .getBookInfoId();
        bookDao.saveBook("qunar001", bookInfoID, BookStatus.INLIBRARY, 15);
        Assert.assertEquals(bookDao.selectBookList(new RowBounds(0, 10), 15)
                .size(), count + 1);
    }

    @Test
    public void testSelectBorrowedCountByBookId() {
        Assert.assertEquals(bookDao.selectBorrowedCountByBookId("qunar001"), 0);
    }

    @Test
    @Transactional
    public void testSaveNav() {
        Assert.assertTrue(1 == bookDao.saveNav("计算机", "编程"));
    }

    @Test
    @Transactional
    public void testSelectNavExistsTitle() {
        bookDao.saveNav("计算机", "编程");
        bookDao.saveNav("计算机", "技术");
        bookDao.saveNav("计算机", "数据库");
        Assert.assertEquals(bookDao.selectNav("计算机").size(), 3);
    }

    @Test
    @Transactional
    public void testSelectNavNotExistsTitle() {
        bookDao.saveNav("计算机", "编程");
        bookDao.saveNav("计算机", "技术");
        bookDao.saveNav("计算机", "数据库");
        Assert.assertEquals(bookDao.selectNav("").size(), 3);
    }

    @Test
    @Transactional
    public void testSelectBookCount() {
        Book book = new Book();
        book.setBookName("java编程思想");
        book.setBookAuthor("jack");
        book.setBookPress("清华出版社");
        book.setBookIntro("java introduction");
        book.setImageURL("http://www.java.jpg");
        book.setBookHot(0);
        book.setNavID(1);
        bookDao.saveBookInfo(book);
        int bookInfoID = bookDao.selectBookInfo("java编程思想", "jack", 1)
                .getBookInfoId();
        bookDao.saveBook("qunar001", bookInfoID, BookStatus.INLIBRARY, 15);
        Assert.assertEquals(bookDao.selectBookCount(bookInfoID, null), 1);
    }

    @Test
    @Transactional
    public void TestSelectBookInfoIdByBookId() {
        Book book = new Book();
        book.setBookName("java编程思想");
        book.setBookAuthor("jack");
        book.setBookPress("清华出版社");
        book.setBookIntro("java introduction");
        book.setImageURL("http://www.java.jpg");
        book.setBookHot(0);
        book.setNavID(1);
        bookDao.saveBookInfo(book);
        int bookInfoID = bookDao.selectBookInfo("java编程思想", "jack", 1)
                .getBookInfoId();
        bookDao.saveBook("qunar001", bookInfoID, BookStatus.INLIBRARY, 15);
        Assert.assertEquals(bookDao.selectBookInfoIdByBookId("qunar001"), bookInfoID);
    }

    /**
     * @author hang.gao
     */
    @Test
    @Transactional
    @Rollback(true)
    @Ignore
    public void testSelectBookByNames() {
        bookDao.saveBook("qunar001", 1, BookStatus.INLIBRARY, 15);
        List<Book> books = bookDao.selectBookByNames(Arrays.asList("qunar001"));
        Assert.assertEquals(1, books.size());
    }

    /**
     * @author hang.gao
     */
    @Test
    @Ignore
    public void testSelectBookByType() {
        List<Book> books = bookDao.selectByType(1, (RowBounds) pageFactory.newPage(0, 20));
        Assert.assertEquals(5, books.size());
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSelectTopByType() {
        List<Book> books = bookDao.selectTop(1, (RowBounds) pageFactory.newPage(0, 10));
        Assert.assertEquals(4, books.size());
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSelectBookReserveCountByBookInfoAndLibrary() {
        Assert.assertEquals(2, bookDao.selectBookReserveCountByBookInfoAndLibrary(1, 1));
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testCountBookByType() {
        Assert.assertEquals(5, bookDao.countBookByType(1));
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSelectBookByBookId() {
        Book book = bookDao.selectBookByBookId("9787111104414");
        Assert.assertEquals("Java编程思想", book.getBookName());
    }

    /**
     * author : libin.chen
     * param : id
     * return : map
     */
    @Test
    @Ignore
    public void testGetBookById() {
        org.junit.Assert.assertNotNull(bookDao.getBookByBookId("D4-1"));
    }

    //System.out.println(bookDao.getBookByBookId(2));
    public void test_getBookByBookId_success() {
        String bookId = "E5-1";

        Map map = bookDao.getBookByBookId(bookId);

        org.junit.Assert.assertEquals(map.get(bookId), bookId);

    }

    @Test
    public void test_getBookByBookId_fail() {
        org.junit.Assert.assertNull(bookDao.getBookByBookId("E5-1-100"));
    }

    @Test
    public void test_getBookInfoIdByBookId() {
        String bookId = "E5-1";
        int bookInfoId = bookDao.getBookInfoIdByBookId(bookId);
        org.junit.Assert.assertEquals(bookInfoId, 1);
    }

    @Test
    public void test_getBookInfoIdByBookId_fail_return_null() {
        String bookId = "E5-1-100";
        org.junit.Assert.assertNull(bookDao.getBookInfoIdByBookId(bookId));
    }

    @Test
    public void test_updateBookInf_success() {

        String bookName = "java编程思想-无线";
        String bookAuthor = "孙三java";
        String bookPress = "清华出版社";
        String bookIntro = "java编程思想是一本优秀的书gaga";
        int navId = 6;
        int bookInfoId = 13;

        Assert.assertEquals(1, bookDao.updateBookInfo(bookName, bookAuthor, bookPress, bookIntro, navId, bookInfoId));
    }


    @Test
    public void test_updateBookInfo_fail() {
        String bookName = "java编程思想-无线";
        String bookAuthor = "孙三java";
        String bookPress = "清华出版社";
        String bookIntro = "java编程思想是一本优秀的书gaga";
        int navId = 6;
        int bookInfoId = -1;

        int count = bookDao.updateBookInfo(bookName, bookAuthor, bookPress, bookIntro, navId, bookInfoId);
        // count 为影响的行数
        Assert.assertEquals(0, count);
    }

    @Test
    public void test_deleteBookOfLib() {
        bookDao.deleteBookOfLib(1, BookStatus.DELETED);
    }
}
