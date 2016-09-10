package com.qunar.fresh.librarysystem.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.qunar.fresh.librarysystem.dao.MyBatisPageFactory;
import com.qunar.fresh.librarysystem.dao.PageFactory;
import org.apache.ibatis.session.RowBounds;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.qunar.fresh.librarysystem.dao.BookDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.search.SearchResult;
import com.qunar.fresh.librarysystem.search.book.BookSearchEngine;
import com.qunar.fresh.librarysystem.search.book.BookSearchResult;
import com.qunar.fresh.librarysystem.service.BookService.BookWrapper;
import com.qunar.fresh.librarysystem.service.impl.BookServiceImpl;

/**
 * @author hang.gao
 */
@RunWith(EasyMockRunner.class)
public class BookServiceGaoTest {

    @Mock
    private BookDao bookDao;

    @Mock
    private BookSearchEngine bookSearchEngine;

    private PageFactory factory = new MyBatisPageFactory();

    private BookServiceImpl getBookService() {
        BookServiceImpl bookService = new BookServiceImpl();
        bookService.setBookDao(bookDao);
        bookService.setBookSearchEngine(bookSearchEngine);
        bookService.setPageFactory(factory);
        return bookService;
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSearchBook() {
        List<Book> books = Arrays.asList(new Book());
        EasyMock.expect(bookSearchEngine.searchBook("Java", 0, 10))
                .andReturn(
                        new SearchResult(2, Arrays.asList("Thinking in Java",
                                "Java编程思想")));
        EasyMock.expect(
                bookDao.selectBookByNames(Arrays.asList("Thinking in Java",
                        "Java编程思想"))).andReturn(books);
        EasyMock.expect(bookDao.selectBookCountByBookInfo(0)).andReturn(2);
        EasyMock.expect(bookDao.selectBookReserveCountByBookInfoAndLibrary(0, 0)).andReturn(2);
        EasyMock.replay(bookDao, bookSearchEngine);
        BookServiceImpl bookService = getBookService();
        BookSearchResult result = bookService.searchBook("Java", 1, 10);
        Assert.assertEquals(result.getPage(), 1);
        Assert.assertEquals(result.getPageSize(), 10);
        Assert.assertEquals(result.getTotalHints(), 2);
        Assert.assertEquals(result.getBooks(), books);
        EasyMock.verify(bookDao, bookSearchEngine);

    }

    /**
     * 测试，当搜索结果为空列表时
     *
     * @author hang.gao
     */
    @Test
    public void testSearchBook_when_result_is_empty() {
        EasyMock.expect(bookSearchEngine.searchBook("Java", 0, 10))
                .andReturn(
                        new SearchResult(0, Collections.<String>emptyList()));
        EasyMock.replay(bookSearchEngine);
        BookServiceImpl bookService = getBookService();
        BookSearchResult result = bookService.searchBook("Java", 1, 10);
        Assert.assertEquals(result.getPage(), 1);
        Assert.assertEquals(result.getPageSize(), 10);
        Assert.assertEquals(result.getTotalHints(), 0);
        Assert.assertEquals(result.getBooks(), Collections.<Book>emptyList());
        EasyMock.verify(bookSearchEngine);
    }

    /**
     * 测试，当搜索结果为空列表时
     *
     * @author hang.gao
     */
    @Test
    public void testSearchBook_when_result_is_null() {
        EasyMock.expect(bookSearchEngine.searchBook("Java", 0, 10))
                .andReturn(null);
        EasyMock.replay(bookSearchEngine);
        BookServiceImpl bookService = getBookService();
        BookSearchResult result = bookService.searchBook("Java", 1, 10);
        Assert.assertEquals(result.getPage(), 1);
        Assert.assertEquals(result.getPageSize(), 10);
        Assert.assertEquals(result.getTotalHints(), 0);
        Assert.assertEquals(result.getBooks(), Collections.<Book>emptyList());
        EasyMock.verify(bookSearchEngine);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchBook_when_keywords_is_null() {
        BookServiceImpl bookService = getBookService();
        bookService.searchBook(null, 1, 100);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchBook_when_keywords_is_empty() {
        BookServiceImpl bookService = getBookService();
        bookService.searchBook("", 1, 100);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchBook_when_page_is_less_than_1() {
        BookServiceImpl bookService = getBookService();
        bookService.searchBook("", 0, 100);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchBook_when_pageSize_is_less_than_1() {
        BookServiceImpl bookService = getBookService();
        bookService.searchBook("", 1, 0);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSearchBook_when_search_result_list_is_empty() {
        EasyMock.expect(bookSearchEngine.searchBook("Java", 0, 10))
                .andReturn(SearchResult.NONE);
        EasyMock.replay(bookDao, bookSearchEngine);
        BookServiceImpl bookService = getBookService();
        BookSearchResult result = bookService.searchBook("Java", 1, 10);
        Assert.assertEquals(result.getPage(), 1);
        Assert.assertEquals(result.getPageSize(), 10);
        Assert.assertEquals(result.getTotalHints(), 0);
        Assert.assertEquals(result.getBooks(), Collections.<Book>emptyList());
        EasyMock.verify(bookDao, bookSearchEngine);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testSearchBook_when_search_result_is_null() {
        EasyMock.expect(bookSearchEngine.searchBook("Java", 0, 10))
                .andReturn(null);
        EasyMock.replay(bookSearchEngine);
        BookServiceImpl bookService = getBookService();
        BookSearchResult result = bookService.searchBook("Java", 1, 10);
        Assert.assertEquals(result.getPage(), 1);
        Assert.assertEquals(result.getPageSize(), 10);
        Assert.assertEquals(result.getTotalHints(), 0);
        Assert.assertEquals(result.getBooks(), Collections.<Book>emptyList());
        EasyMock.verify(bookSearchEngine);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testTop() {
        List<Book> books = Arrays.asList(new Book());
        EasyMock.expect(bookDao.selectTop(1, (RowBounds) factory.newPage(0, 10))).andReturn(books);
        EasyMock.expect(bookDao.selectBookCountByBookInfo(0)).andReturn(1);
        EasyMock.expect(bookDao.selectBookReserveCountByBookInfoAndLibrary(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(1);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        List<Book> result = bookService.top(10, 1);
        Assert.assertEquals(1, result.size());
        EasyMock.verify(bookDao);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testTop_when_result_is_empty() {
        EasyMock.expect(bookDao.selectTop(1, (RowBounds) factory.newPage(0, 10))).andReturn(Collections.<Book>emptyList());
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        List<Book> result = bookService.top(10, 1);
        Assert.assertEquals(0, result.size());
        EasyMock.verify(bookDao);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testTop_when_result_is_null_and_return_empty_list() {
        EasyMock.expect(bookDao.selectTop(1, (RowBounds) factory.newPage(0, 10))).andReturn(null);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        List<Book> result = bookService.top(10, 1);
        Assert.assertEquals(0, result.size());
        EasyMock.verify(bookDao);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testTop_when_type_is_not_exists() {
        List<Book> books = Collections.emptyList();
        EasyMock.expect(bookDao.selectTop(123, (RowBounds) factory.newPage(0, 10))).andReturn(books);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        List<Book> result = bookService.top(10, 123);
        Assert.assertEquals(0, result.size());
        EasyMock.verify(bookDao);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTop_when_topn_param_is_illegal() {
        BookServiceImpl bookService = getBookService();
        bookService.top(0, 123);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTop_when_type_id_is_illegal() {
        BookServiceImpl bookService = getBookService();
        bookService.top(23, -1);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testBookOfType() {
        List<Book> books = Arrays.asList(new Book());
        EasyMock.expect(bookDao.selectByType(1, (RowBounds) factory.newPage(0, 10))).andReturn(books);
        EasyMock.expect(bookDao.selectBookReserveCountByBookInfoAndLibrary(0, 0)).andReturn(0);
        EasyMock.expect(bookDao.selectBookCountByBookInfo(0)).andReturn(2);
        EasyMock.expect(bookDao.countBookByType(1)).andReturn(2L);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        BookWrapper result = bookService.bookOfType(1, 1, 10);
        Assert.assertEquals(2, result.getTotalCount());
        Assert.assertArrayEquals(books.toArray(), result.getBooks().toArray());
        EasyMock.verify(bookDao);
    }

    /**
     * 当输入的类型id参数不合法时
     *
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBookOfType_when_type_id_is_illegal() {
        BookServiceImpl bookService = getBookService();
        bookService.bookOfType(0, 1, 10);
    }

    /**
     * 测试当输入的页码参数不合法时
     *
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBookOfType_when_page_is_illegal() {
        BookServiceImpl bookService = getBookService();
        bookService.bookOfType(1, 0, 10);
    }

    /**
     * 测试当输入的每页大小参数不合法时
     *
     * @author hang.gao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBookOfType_when_pageSize_is_illegal() {
        BookServiceImpl bookService = getBookService();
        bookService.bookOfType(1, 1, 0);
    }

    /**
     * 测试当输入的每页大小参数不合法时
     *
     * @author hang.gao
     */
    @Test
    public void testBookOfType_when_result_list_is_empty() {
        EasyMock.expect(bookDao.selectByType(1, (RowBounds) factory.newPage(0, 10))).andReturn(Collections.<Book>emptyList());
        EasyMock.expect(bookDao.countBookByType(1)).andReturn(0l);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        BookWrapper result = bookService.bookOfType(1, 1, 10);
        Assert.assertEquals(0, result.getTotalCount());
        EasyMock.verify(bookDao);
    }

    /**
     * 测试当输入的每页大小参数不合法时
     *
     * @author hang.gao
     */
    @Test
    public void testBookOfType_when_result_list_is_null_and_return_empty_list() {
        EasyMock.expect(bookDao.selectByType(1, (RowBounds) factory.newPage(0, 10))).andReturn(null);
        EasyMock.expect(bookDao.countBookByType(1)).andReturn(0l);
        EasyMock.replay(bookDao);
        BookServiceImpl bookService = getBookService();
        BookWrapper result = bookService.bookOfType(1, 1, 10);
        Assert.assertEquals(0, result.getTotalCount());
        EasyMock.verify(bookDao);
    }

}
