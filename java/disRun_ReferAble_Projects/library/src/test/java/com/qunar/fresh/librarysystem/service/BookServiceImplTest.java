package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.dao.BookDao;
import com.qunar.fresh.librarysystem.model.Book;

import com.qunar.fresh.librarysystem.service.impl.BookServiceImpl;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: libin.chen
 * Date: 14-4-06
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class BookServiceImplTest {

    @Resource
    private BookServiceImpl bookService;

    @Test
    //@Ignore
    public void test_getBookByBookId() throws IOException {

        BookDao bookDao = mock(BookDao.class);

        bookService.setBookDao(bookDao);

        when(bookDao.getBookByBookId("E5-4")).thenReturn(Maps.newHashMap());

        bookService.getBookByBookId("E5-4");

        verify(bookDao).getBookByBookId("E5-4");

    }

    @Test
    //@Ignore
    public void test_modifyBookInfo_navId_is_null_second_selectBookNavId_return_not_null_updateBookInfo_return_1() throws IOException {
        BookDao bookDao = mock(BookDao.class);

        LogService logService = mock(LogService.class);

        bookService.setBookDao(bookDao);

        Book book = mock(Book.class);

        when(book.getBookId()).thenReturn("E5-4");
        when(book.getId()).thenReturn(4);

        when(bookDao.getBookInfoIdByBookId(book.getBookId())).thenReturn(4);

        when(book.getTitle()).thenReturn("计算机大类");
        when(book.getBookType()).thenReturn("后端");
        when(book.getBookLib()).thenReturn(5);

        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(null).thenReturn(1);


        when(book.getBookName()).thenReturn("Maven实战-技术");
        when(book.getBookAuthor()).thenReturn("李四Maven");
        when(book.getBookPress()).thenReturn("清华出版社");
        when(book.getBookIntro()).thenReturn("Maven实战是一本好书");
        when(book.getBookInfoId()).thenReturn(4);

        when(bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId())).thenReturn(1);

        org.junit.Assert.assertEquals(bookService.modifyBookInfo(book, "libin.chen"), 1);
        verify(bookDao).saveNav(book.getTitle(), book.getBookType());
    }

    @Test
    //@Ignore
    public void test_modifyBookInfo_navId_is_null_second_selectBookNavId_return_not_null_updateBookInfo_return_0() throws IOException {
        BookDao bookDao = mock(BookDao.class);

        LogService logService = mock(LogService.class);

        bookService.setBookDao(bookDao);

        Book book = mock(Book.class);

        when(book.getBookId()).thenReturn("E5-4");
        when(book.getId()).thenReturn(4);

        when(bookDao.getBookInfoIdByBookId(book.getBookId())).thenReturn(4);

        when(book.getTitle()).thenReturn("计算机大类");
        when(book.getBookType()).thenReturn("后端");
        when(book.getBookLib()).thenReturn(5);

        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(null).thenReturn(0);


        when(book.getBookName()).thenReturn("Maven实战-技术");
        when(book.getBookAuthor()).thenReturn("李四Maven");
        when(book.getBookPress()).thenReturn("清华出版社");
        when(book.getBookIntro()).thenReturn("Maven实战是一本好书");
        when(book.getBookInfoId()).thenReturn(4);

        when(bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId())).thenReturn(1);

        org.junit.Assert.assertEquals(bookService.modifyBookInfo(book, "libin.chen"), 0);
        verify(bookDao).saveNav(book.getTitle(), book.getBookType());
    }

    @Test
    //@Ignore
    public void test_modifyBookInfo_navId_is_null_second_selectBookNavId_return_null() throws IOException {
        BookDao bookDao = mock(BookDao.class);

        LogService logService = mock(LogService.class);

        bookService.setBookDao(bookDao);

        Book book = mock(Book.class);

        when(book.getBookId()).thenReturn("E5-4");
        when(book.getId()).thenReturn(4);

        when(bookDao.getBookInfoIdByBookId(book.getBookId())).thenReturn(4);

        when(book.getTitle()).thenReturn("计算机大类");
        when(book.getBookType()).thenReturn("后端");
        when(book.getBookLib()).thenReturn(5);

        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(null).thenReturn(null);


        when(book.getBookName()).thenReturn("Maven实战-技术");
        when(book.getBookAuthor()).thenReturn("李四Maven");
        when(book.getBookPress()).thenReturn("清华出版社");
        when(book.getBookIntro()).thenReturn("Maven实战是一本好书");
        when(book.getBookInfoId()).thenReturn(4);

        when(bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId())).thenReturn(1);

        org.junit.Assert.assertEquals(bookService.modifyBookInfo(book, "libin.chen"), 0);

        verify(bookDao).saveNav(book.getTitle(), book.getBookType());
    }

    @Test
    //@Ignore
    public void test_modifyBookInfo_navId_not_null_updateBookInfo_return_1() throws IOException {
        BookDao bookDao = mock(BookDao.class);

        LogService logService = mock(LogService.class);

        bookService.setBookDao(bookDao);

        Book book = mock(Book.class);

        when(book.getBookId()).thenReturn("E5-4");
        when(book.getId()).thenReturn(4);

        when(bookDao.getBookInfoIdByBookId(book.getBookId())).thenReturn(4);

        when(book.getTitle()).thenReturn("计算机大类");
        when(book.getBookType()).thenReturn("后端");
        when(book.getBookLib()).thenReturn(5);

        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(1);


        when(book.getBookName()).thenReturn("Maven实战-技术");
        when(book.getBookAuthor()).thenReturn("李四Maven");
        when(book.getBookPress()).thenReturn("清华出版社");
        when(book.getBookIntro()).thenReturn("Maven实战是一本好书");
        when(book.getBookInfoId()).thenReturn(4);

        when(bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId())).thenReturn(1);

        org.junit.Assert.assertEquals(bookService.modifyBookInfo(book, "libin.chen"), 1);

        verify(bookDao).updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId());
    }

    @Test
    //@Ignore
    public void test_modifyBookInfo_navId_not_null_updateBookInfo_return_0() throws IOException {
        BookDao bookDao = mock(BookDao.class);

        LogService logService = mock(LogService.class);

        bookService.setBookDao(bookDao);

        Book book = mock(Book.class);

        when(book.getBookId()).thenReturn("E5-4");
        when(book.getId()).thenReturn(4);

        when(bookDao.getBookInfoIdByBookId(book.getBookId())).thenReturn(4);

        when(book.getTitle()).thenReturn("计算机大类");
        when(book.getBookType()).thenReturn("后端");
        when(book.getBookLib()).thenReturn(5);

        when(bookDao.selectBookNavId(book.getTitle(), book.getBookType())).thenReturn(1);


        when(book.getBookName()).thenReturn("Maven实战-技术");
        when(book.getBookAuthor()).thenReturn("李四Maven");
        when(book.getBookPress()).thenReturn("清华出版社");
        when(book.getBookIntro()).thenReturn("Maven实战是一本好书");
        when(book.getBookInfoId()).thenReturn(4);

        when(bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId())).thenReturn(0);

        org.junit.Assert.assertEquals(bookService.modifyBookInfo(book, "libin.chen"), 0);

        verify(bookDao).updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(), book.getBookIntro(), 1, book.getBookInfoId());
    }

}

