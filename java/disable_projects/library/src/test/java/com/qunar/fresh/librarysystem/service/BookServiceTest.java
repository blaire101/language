package com.qunar.fresh.librarysystem.service;

import com.qunar.fresh.librarysystem.crawler.BookExtractor;
import com.qunar.fresh.librarysystem.crawler.BookHtmlExtractor;
import com.qunar.fresh.librarysystem.dao.BookDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import com.qunar.fresh.librarysystem.search.book.BookSearchEngine;
import com.qunar.fresh.librarysystem.service.impl.BookServiceImpl;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import org.junit.Assert;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA. User: jinglv Date: 14-3-28 Time: 下午1:19
 *
 * @author hang.gao
 * @author jing.lv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class BookServiceTest {

    @Resource
    private BookServiceImpl bookService;

    @Test
    public void testAddBookExistsBook() {
        BookDao bookDao = mock(BookDao.class);
        Book book = mock(Book.class);
        bookService.setBookDao(bookDao);
        when(book.getBookId()).thenReturn("qunar010");
        when(bookDao.selectBookCountByBookId("qunar010")).thenReturn(1);
        String msg = bookService.addBook(book, "jing.lv");
        verify(bookDao).selectBookCountByBookId("qunar010");
        Assert.assertEquals(msg, "该书已经存在，不允许重复添加");
    }

    @Test
    public void testAddBookNotExistsBookAndNavAndNoException() {
        BookDao bookDao = mock(BookDao.class);
        LogService logService = mock(LogService.class);
        BookExtractor bookExtractor = mock(BookHtmlExtractor.class);
        BookSearchEngine bookSearchEngine = mock(BookSearchEngine.class);
        ReserveService reserveService = mock(ReserveService.class);
        Book book = mock(Book.class);
        when(book.getBookId()).thenReturn("qunar010");
        when(book.getBookName()).thenReturn("围城");
        when(book.getBookAuthor()).thenReturn("钱钟书");
        when(book.getBookLib()).thenReturn(15);
        when(book.getBookType()).thenReturn("生活");
        when(book.getBookPress()).thenReturn("");
        bookService.setBookDao(bookDao);
        bookService.setLogService(logService);
        bookService.setBookExtractor(bookExtractor);
        bookService.setBookSearchEngine(bookSearchEngine);
        bookService.setReserveService(reserveService);
        Book bookInfo = mock(Book.class);
        when(bookInfo.getImageUrl()).thenReturn("http://www.a.jpg");
        when(bookInfo.getBookIntro()).thenReturn("围城是一本文学小说");
        when(bookInfo.getBookPress()).thenReturn("机械工业出版社");
        when(bookDao.selectBookCountByBookId(book.getBookId())).thenReturn(0);
        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(null).thenReturn(1);
        when(bookExtractor.generate(book.getBookName())).thenReturn(bookInfo);
        Book result = mock(Book.class);
        when(result.getBookInfoId()).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId())).thenReturn(result);
        when(bookDao.selectBookCount(book.getBookInfoId(), null)).thenReturn(1);
        String msg = bookService.addBook(book, "jing.lv");
        verify(bookDao).saveNav(book.getTitle(), book.getBookType());
        verify(book).setNavID(1);
        verify(book).setImageURL("http://www.a.jpg");
        verify(book).setBookIntro("围城是一本文学小说");
        verify(book).setBookPress("机械工业出版社");
        verify(book).setBookHot(0);
        verify(bookDao).saveBookInfo(book);
        verify(book).setBookInfoId(1);
        verify(bookDao).saveBook(book.getBookId(), 1, book.getBookStatus(), book.getBookLib());
        verify(bookSearchEngine).apendToIndex(book);
        verify(reserveService).dealReminderReserveUser();
        Assert.assertEquals(msg, "");
    }

    @Test
    public void testAddBookNotExistsBookAndNavAndException() {
        BookDao bookDao = mock(BookDao.class);
        LogService logService = mock(LogService.class);
        BookExtractor bookExtractor = mock(BookHtmlExtractor.class);
        BookSearchEngine bookSearchEngine = mock(BookSearchEngine.class);
        ReserveService reserveService = mock(ReserveService.class);
        Book book = mock(Book.class);
        when(book.getBookId()).thenReturn("qunar010");
        when(book.getBookName()).thenReturn("围城");
        when(book.getBookAuthor()).thenReturn("钱钟书");
        when(book.getBookLib()).thenReturn(15);
        when(book.getBookType()).thenReturn("生活");
        when(book.getBookPress()).thenReturn("");
        bookService.setBookDao(bookDao);
        bookService.setLogService(logService);
        bookService.setBookExtractor(bookExtractor);
        bookService.setBookSearchEngine(bookSearchEngine);
        bookService.setReserveService(reserveService);
        when(bookDao.selectBookCountByBookId(book.getBookId())).thenReturn(0);
        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(null).thenReturn(1);
        when(bookExtractor.generate(book.getBookName())).thenThrow(new RuntimeException());
        Book bookInfo = mock(Book.class);
        when(bookInfo.getBookInfoId()).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId())).thenReturn(bookInfo);
        when(bookDao.selectBookCount(book.getBookInfoId(), null)).thenReturn(1);
        String msg = bookService.addBook(book, "jing.lv");
        verify(bookDao).saveNav(book.getTitle(), book.getBookType());
        verify(book).setNavID(1);
        verify(book).setImageURL("");
        verify(book).setBookIntro("");
        verify(book).setBookHot(0);
        verify(bookDao).saveBookInfo(book);
        verify(book).setBookInfoId(1);
        verify(bookDao).saveBook(book.getBookId(), 1, book.getBookStatus(), book.getBookLib());
        verify(bookSearchEngine).apendToIndex(book);
        verify(reserveService).dealReminderReserveUser();
        Assert.assertEquals(msg, "");
    }

    @Test
    public void testAddBookNotExistsBookAndBookInfoAndExistsNav() {
        BookDao bookDao = mock(BookDao.class);
        LogService logService = mock(LogService.class);
        BookExtractor bookExtractor = mock(BookHtmlExtractor.class);
        BookSearchEngine bookSearchEngine = mock(BookSearchEngine.class);
        ReserveService reserveService = mock(ReserveService.class);
        Book book = mock(Book.class);
        when(book.getBookId()).thenReturn("qunar010");
        when(book.getBookName()).thenReturn("围城");
        when(book.getBookAuthor()).thenReturn("钱钟书");
        when(book.getBookLib()).thenReturn(15);
        when(book.getBookType()).thenReturn("生活");
        when(book.getBookPress()).thenReturn("");
        bookService.setBookDao(bookDao);
        bookService.setLogService(logService);
        bookService.setBookExtractor(bookExtractor);
        bookService.setBookSearchEngine(bookSearchEngine);
        bookService.setReserveService(reserveService);
        when(bookDao.selectBookCountByBookId(book.getBookId())).thenReturn(0);
        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), 1)).thenReturn(null);
        when(bookExtractor.generate(book.getBookName())).thenThrow(new RuntimeException());
        Book bookInfo = mock(Book.class);
        when(bookInfo.getBookInfoId()).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId())).thenReturn(bookInfo);
        when(bookDao.selectBookCount(book.getBookInfoId(), null)).thenReturn(1);
        String msg = bookService.addBook(book, "jing.lv");
        verify(book).setImageURL("");
        verify(book).setBookIntro("");
        verify(book).setBookHot(0);
        verify(bookDao).saveBookInfo(book);
        verify(book).setBookInfoId(1);
        verify(bookDao).saveBook(book.getBookId(), 1, book.getBookStatus(), book.getBookLib());
        verify(bookSearchEngine).apendToIndex(book);
        verify(reserveService).dealReminderReserveUser();
        Assert.assertEquals(msg, "");
    }

    @Test
    public void testAddBookNotExistsBookAndExistsNavAndBookInfo() {
        BookDao bookDao = mock(BookDao.class);
        LogService logService = mock(LogService.class);
        BookExtractor bookExtractor = mock(BookHtmlExtractor.class);
        BookSearchEngine bookSearchEngine = mock(BookSearchEngine.class);
        ReserveService reserveService = mock(ReserveService.class);
        Book book = mock(Book.class);
        when(book.getBookId()).thenReturn("qunar010");
        when(book.getBookName()).thenReturn("围城");
        when(book.getBookAuthor()).thenReturn("钱钟书");
        when(book.getBookLib()).thenReturn(15);
        when(book.getBookType()).thenReturn("生活");
        when(book.getBookPress()).thenReturn("");
        bookService.setBookDao(bookDao);
        bookService.setLogService(logService);
        bookService.setBookExtractor(bookExtractor);
        bookService.setBookSearchEngine(bookSearchEngine);
        bookService.setReserveService(reserveService);
        when(bookDao.selectBookCountByBookId(book.getBookId())).thenReturn(0);
        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), 1)).thenReturn(null);
        when(bookExtractor.generate(book.getBookName())).thenThrow(new RuntimeException());
        Book bookInfo = mock(Book.class);
        when(bookInfo.getBookInfoId()).thenReturn(1);
        when(bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId())).thenReturn(bookInfo);
        when(bookDao.selectBookCount(book.getBookInfoId(), null)).thenReturn(1);
        String msg = bookService.addBook(book, "jing.lv");
        when(bookDao.selectBookInfoIdByBookId(book.getBookId())).thenReturn(1);
        verify(book).setBookInfoId(1);
        verify(bookDao).saveBook(book.getBookId(), 1, book.getBookStatus(), book.getBookLib());
        when(bookDao.selectBookCount(book.getBookInfoId(), null)).thenReturn(1);
        verify(bookSearchEngine).apendToIndex(book);
        verify(reserveService).dealReminderReserveUser();
        Assert.assertEquals(msg, "");
    }

    @Test
    public void testGetBookList() {
        BookDao bookDao = mock(BookDao.class);
        bookService.setBookDao(bookDao);
        bookService.getBookList(0, 10, 15);
        verify(bookDao).selectBookList(any(RowBounds.class), anyInt());
    }

    @Test
    public void testGetBookCountInOneLib() {
        BookDao bookDao = mock(BookDao.class);
        bookService.setBookDao(bookDao);
        when(bookDao.selectBookCount(null, 15)).thenReturn(10);
        int count = bookService.getBookCountInOneLib(15);
        verify(bookDao).selectBookCount(null, 15);
        Assert.assertEquals(10, count);
    }

    @Test
    public void testDeleteBookNotExistsBorrowedAndUpdateFail() {
        BookDao bookDao = mock(BookDao.class);
        Logger logger = mock(Logger.class);
        bookService.setBookDao(bookDao);
        bookService.setLogger(logger);
        when(bookDao.selectBookList(any(RowBounds.class), anyInt())).thenReturn(new ArrayList<Book>());
        when(bookDao.selectBorrowedCountByBookId("qunar001")).thenReturn(0);
        when(bookDao.selectBookStatusByBookId("qunar001")).thenReturn(BookStatus.DELETED);
        Map<String, Object> resultMap = bookService.deleteBook(10, 0, "qunar001", 15, "jing.lv");
        verify(bookDao).selectBorrowedCountByBookId("qunar001");
        verify(bookDao).selectBookStatusByBookId("qunar001");
        verify(logger).error("数据库删除图书" + "qunar001" + "失败");
        Assert.assertEquals(resultMap.get("errmsg"), "删除失败");
    }

    @Test
    public void testDeleteBookNotExistsBorrowedAndUpdateSuccess() {
        BookDao bookDao = mock(BookDao.class);
        BookSearchEngine bookSearchEngine = mock(BookSearchEngine.class);
        bookService.setBookDao(bookDao);
        bookService.setBookSearchEngine(bookSearchEngine);
        when(bookDao.selectBorrowedCountByBookId("qunar001")).thenReturn(0);
        when(bookDao.updateBookStatus("qunar001", BookStatus.DELETED)).thenReturn(1);
        when(bookDao.selectBookInfoIdByBookId("qunar001")).thenReturn(3);
        when(bookDao.selectBookCount(3, null)).thenReturn(0);
        Map<String, Object> resultMap = bookService.deleteBook(10, 0, "qunar001", 15, "jing.lv");
        verify(bookDao).selectBorrowedCountByBookId("qunar001");
        verify(bookDao).updateBookStatus("qunar001", BookStatus.DELETED);
        verify(bookSearchEngine).deleteIndex(3);
        Assert.assertEquals(resultMap.get("errmsg"), "删除成功");
    }

    @Test
    public void testDeleteBookExistsBorrowed() {
        BookDao bookDao = mock(BookDao.class);
        bookService.setBookDao(bookDao);
        when(bookDao.selectBorrowedCountByBookId("qunar001")).thenReturn(1);
        Map<String, Object> resultMap = bookService.deleteBook(10, 0, "qunar001", 15, "jing.lv");
        verify(bookDao).selectBorrowedCountByBookId("qunar001");
        Assert.assertEquals(resultMap.get("errmsg"), "此书被借，不允许删除");
    }

    @Test
    public void testAddBookLoad() {
        BookDao bookDao = mock(BookDao.class);
        bookService.setBookDao(bookDao);
        bookService.addBookLoad("计算机");
        verify(bookDao).selectNav("计算机");
    }
}

