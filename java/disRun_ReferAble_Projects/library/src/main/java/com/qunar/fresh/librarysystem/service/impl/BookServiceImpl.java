package com.qunar.fresh.librarysystem.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.Page;
import com.qunar.fresh.librarysystem.dao.PageFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.qunar.fresh.librarysystem.crawler.BookExtractor;
import com.qunar.fresh.librarysystem.dao.BookDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.search.AnalyzerFactory;
import com.qunar.fresh.librarysystem.search.SearchResult;
import com.qunar.fresh.librarysystem.search.book.BookSearchEngine;
import com.qunar.fresh.librarysystem.search.book.BookSearchResult;
import com.qunar.fresh.librarysystem.service.BookService;
import com.qunar.fresh.librarysystem.service.ReserveService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA. User: jinglv Date: 14-3-28 Time: 下午5:45
 *
 * @author jing.lv
 * @author hang.gao
 * @author libin.chen
 */
public class BookServiceImpl implements BookService, DisposableBean, InitializingBean {

    public static final String DEFAULT_INTRO = "暂无书籍简介";
    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Resource
    private LogService logService;

    @Resource
    private ReserveService reserveService;

    @Resource
    private BookExtractor bookExtractor;
    /**
     * 访问图书表
     */
    @Resource
    private BookDao bookDao;

    @Resource
    private PageFactory pageFactory;
    /**
     * 图书搜索
     */
    @Resource
    private BookSearchEngine bookSearchEngine;

    private ExecutorService executorService;

    private int crawlThreadSize;

    public BookServiceImpl() {
    }

    public BookServiceImpl(BookExtractor bookExtractor, BookDao bookDao, LogService logService) {
        this.bookExtractor = bookExtractor;
        this.bookDao = bookDao;
        this.logService = logService;
    }

    /* 根据title和bookLib查询分类 */
    @Override
    public Set<Category> addBookLoad(String title) {
        return bookDao.selectNav(title);
    }

    /*
     * 添加新书，首先根据输入的书名和作者、所在图书馆查询该书具体信息是否已经存在，若存在，直接将该书的book_id和book_info_id插入book表
     * 若不存在，则首先查询根据该书的分类查询分类表，若存在该分类，则先将该书的具体信息添加到book_info表，然后查询该书的book_info_id，
     * 然后插入book表，若不存在该书分类，则先在分类表中添加新的分类，再插入book_info表，然后插入book表
     */
    @Override
    @Transactional
    public String addBook(Book book, String userRtx) {
        Preconditions.checkNotNull(book);
        if (0 == bookDao.selectBookCountByBookId(book.getBookId())) {
            logger.info("不存在此bookId的图书");
            book.setBookStatus(BookStatus.INLIBRARY);
            Integer navId = bookDao.selectBookNavId(book.getTitle(), book.getBookType());
            if (null == navId) {
                StringBuilder logInfo = new StringBuilder();
                logger.info("不存在此书的分类");
                bookDao.saveNav(book.getTitle(), book.getBookType());
                logService
                        .insertLog(new LogInfo(OperationType.OTHER, new Date(), userRtx, book.getBookLib(),
                                logInfo.append("添加分类").append(book.getTitle()).append(",").append(book.getBookType()).toString()));
                navId = bookDao.selectBookNavId(book.getTitle(), book.getBookType());
                book.setNavID(navId);
                addBookByBookInfo(book, userRtx);
            } else {
                logger.info("存在此书的分类");
                book.setNavID(navId);
                Book result = bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), navId);
                if (result == null) {
                    logger.info("不存在此书的bookInfo");
                    addBookByBookInfo(book, userRtx);
                } else {
                    logger.info("存在此书的bookInfo");
                    if (DEFAULT_INTRO.equals(result.getBookIntro().trim())) {
                        logger.info("此书的书籍简介不正确");
                        addBookByBookInfo(book, userRtx);
                    } else {
                        logger.info("此书的书籍简介正确");
                        storeBook(book, userRtx);
                    }
                }
            }
            return "";
        } else {
            logger.error("此bookId的图书存在或者此bookId已经过期");
            return "该书已经存在，不允许重复添加";
        }
    }

    /* 根据是否存在bookInfo插入新书 */
    private void addBookByBookInfo(final Book book, final String userRtx) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (BookServiceImpl.this) {
                    Book bookInfo = bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId());
                    if (null == bookInfo) {
                        setBookInfoFromWeb(book);
                        bookDao.saveBookInfo(book);
                    } else {
                        if (DEFAULT_INTRO.equals(bookInfo.getBookIntro())) {
                            setBookInfoFromWeb(book);
                            bookDao.updateBookInfo(null, null, book.getBookPress(), book.getBookIntro(), null, bookInfo.getBookInfoId());
                            StringBuilder logInfo = new StringBuilder();
                            logService.insertLog(new LogInfo(OperationType.ALTERBOOK, new Date(), userRtx, book.getBookLib(), logInfo
                                    .append("修改图书 书名：《").append(book.getBookName()).append("》，编号：").append(book.getBookId()).toString()));
                        }
                    }
                }
                storeBook(book, userRtx);
            }
        });
    }

    /*将书添加到book表中*/
    private void storeBook(Book book, String userRtx) {
        int bookInfoId = bookDao.selectBookInfo(book.getBookName(), book.getBookAuthor(), book.getNavId()).getBookInfoId();
        book.setBookInfoId(bookInfoId);
        bookDao.saveBook(book.getBookId(), bookInfoId, book.getBookStatus(), book.getBookLib());
        if (bookDao.selectBookCount(book.getBookInfoId(), null) == 1) {
            bookSearchEngine.apendToIndex(book);
        }
        reserveService.dealReminderReserveUser();
        StringBuilder logInfo = new StringBuilder();
        logService.insertLog(new LogInfo(OperationType.ALTERBOOK, new Date(), userRtx, book.getBookLib(), logInfo
                .append("增加图书 书名：《").append(book.getBookName()).append("》，编号：").append(book.getBookId()).toString()));
    }

    /* 对book进行信息完善 */
    private void setBookInfoFromWeb(Book book) {
        try {
            Book bookInfo = bookExtractor.generate(book.getBookName());
            book.setImageURL(bookInfo.getImageUrl());
            book.setBookIntro(bookInfo.getBookIntro());
            if ("".equals(book.getBookPress())) {
                logger.info("该图书不存在bookPress");
                book.setBookPress(bookInfo.getBookPress());
            }
        } catch (Exception e) {
            logger.error("获取图书信息异常", e);
            book.setImageURL("");
            book.setBookIntro(DEFAULT_INTRO);
            if ("".equals(book.getBookPress())) {
                book.setBookPress("无");
            }
        } finally {
            book.setBookHot(0);
        }
    }

    @Override
    public BookSearchResult searchBook(String keywords, int page, int pageCount) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkArgument(page > 0 && pageCount >= 1);
        SearchResult searchResult = bookSearchEngine.searchBook(keywords, (page - 1) * pageCount, pageCount);
        logger.trace("搜索关键字 {}", keywords);
        return doWithSearchResult(page, pageCount, searchResult);
    }

    /**
     * 处理搜索出来的结果
     *
     * @param page
     * @param pageCount
     * @param searchResult
     * @return
     */
    private BookSearchResult doWithSearchResult(int page, int pageCount, SearchResult searchResult) {
        if (searchResult == null) {
            searchResult = SearchResult.NONE;
        }
        if (searchResult.getSearchResult().size() == 0) {
            logger.trace("没有搜索到结果}");
            // 没有搜索结果
            return new BookSearchResult(searchResult.getTotalBookCount(), Collections.<Book>emptyList(), page,
                    pageCount);
        }
        return new BookSearchResult(searchResult.getTotalBookCount(), findBooks(searchResult), page, pageCount);
    }

    @Override
    public BookSearchResult searchBookByName(String keywords, final int page, final int pageCount) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkArgument(page > 0 && pageCount >= 1);
        SearchResult searchResult = bookSearchEngine.searchBookByName(keywords, new Page() {

            @Override
            public int getOffset() {
                return (page - 1) * pageCount;
            }

            @Override
            public int getLimit() {
                return pageCount;
            }
        });
        logger.trace("搜索图书名 {}", keywords);
        return doWithSearchResult(page, pageCount, searchResult);
    }

    /**
     * 查询图书
     *
     * @param searchResult 搜索结果
     * @return 搜索结果对应的图书列表
     */
    private List<Book> findBooks(SearchResult searchResult) {
        logger.trace("Query books from database");
        final List<Book> books = Optional.fromNullable(bookDao.selectBookByNames(searchResult.getSearchResult())).or(
                Collections.<Book>emptyList());
        queryBookCount(books);
        final List<String> list = searchResult.getSearchResult();
        Collections.sort(books, new Comparator<Book>() {

            @Override
            public int compare(Book src, Book cmp) {
                return Ints.compare(list.indexOf(src.getBookName()), list.indexOf(cmp.getBookName()));
            }
        });
        return books;
    }

    /**
     * 查询图书的数量与可预约的数量
     *
     * @param books 图书列表
     */
    private List<Book> queryBookCount(final List<Book> books) {
        for (Book book : books) {
            book.setBookCount(bookDao.selectBookCountByBookInfo(book.getBookInfoId()));
            book.setReserveCount(bookDao.selectBookReserveCountByBookInfoAndLibrary(book.getBookInfoId(),
                    book.getBookLib()));
        }
        return books;
    }

    /* 根据limit和offset得到对应页码的记录列表 */
    @Override
    public List<Book> getBookList(int limit, int offset, int bookLib) {
        return bookDao.selectBookList(new RowBounds(offset, limit), bookLib);
    }

    /* 根据图书馆id得到该图书馆所有图书数目 */
    @Override
    public int getBookCountInOneLib(int bookLib) {
        return bookDao.selectBookCount(null, bookLib);
    }

    /* 根据bookId删除该书 */
    @Override
    @Transactional
    public Map<String, Object> deleteBook(int limit, int offset, String bookId, int bookLib, String userRtx) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Book> list = getBookList(limit, offset, bookLib);
        if (0 == bookDao.selectBorrowedCountByBookId(bookId)) {
            logger.info("此书没有被借");
            if (BookStatus.DELETED == bookDao.selectBookStatusByBookId(bookId)) {
                logger.info("此书已经删除");
                resultMap.put("errmsg", "删除失败");
                resultMap.put("resultList", list);
                logger.error("数据库删除图书" + bookId + "失败");
            } else {
                Book bookInfo = bookDao.selectBookByBookId(bookId);
                bookDao.updateBookStatus(bookId, BookStatus.DELETED);
                resultMap.put("errmsg", "删除成功");
                resultMap.put("resultList", getBookList(limit, offset, bookLib));
                int bookInfoId = bookDao.selectBookInfoIdByBookId(bookId);
                if (0 == bookDao.selectBookCount(bookInfoId, null)) {
                    bookSearchEngine.deleteIndex(bookInfoId);
                }
                StringBuilder logInfo = new StringBuilder();
                logService.insertLog(new LogInfo(OperationType.DELETEBOOK, new Date(), userRtx, bookLib, logInfo
                        .append("书名：《").append(bookInfo.getBookName()).append("》，编号：").append(bookId).toString()));
            }
        } else {
            resultMap.put("errmsg", "此书被借，不允许删除");
            resultMap.put("resultList", list);
        }
        return resultMap;
    }

    /* 根据bookId更改图书状态 */
    @Override
    public int modifyBookStatus(String bookId, BookStatus bookStatus) {
        return bookDao.updateBookStatus(bookId, bookStatus);
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setBookSearchEngine(BookSearchEngine bookSearchEngine) {
        this.bookSearchEngine = bookSearchEngine;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setBookExtractor(BookExtractor bookExtractor) {
        this.bookExtractor = bookExtractor;
    }

    public static void setLogger(Logger logger) {
        BookServiceImpl.logger = logger;
    }

    public void setReserveService(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @Override
    public List<Book> top(int n, int type) {
        Preconditions.checkArgument(n > 0);
        Preconditions.checkArgument(type >= 0);
        logger.trace("查询图书的top {}，图书的类型id为{}", n, type);
        return queryBookCount(Optional.fromNullable(bookDao.selectTop(type, (RowBounds) pageFactory.newPage(0, n))).or(
                Collections.<Book>emptyList()));
    }

    @Override
    public BookWrapper bookOfType(int typeid, int page, int pageSize) {
        Preconditions.checkArgument(typeid > 0);
        Preconditions.checkArgument(page > 0);
        Preconditions.checkArgument(pageSize > 0);
        logger.trace("按照类型查询图书，图书的类型id为{}，查询图书的第{}页，每页大小为{}", typeid, page, pageSize);
        return new BookWrapper(queryBookCount(Optional.fromNullable(
                bookDao.selectByType(typeid, (RowBounds) pageFactory.newPage((page - 1) * pageSize, pageSize))).or(
                Collections.<Book>emptyList())), (int) bookDao.countBookByType(typeid));
    }

    @Override
    public Book queryBookByBookId(String bookId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bookId));
        return bookDao.selectBookByBookId(bookId);
    }

    @Override
    public void updateBookHot(int bookInfoId) {
        Preconditions.checkArgument(bookInfoId > 0);
        bookDao.updateHot(bookInfoId);
    }

    /**
     * 根据图书表的 ID 得到 图书可更改的详细信息
     *
     * @author : libin.chen
     */

    public Map<String, Object> getBookByBookId(String BookId) {
        return bookDao.getBookByBookId(BookId);
    }

    /**
     * 更改图书信息处理
     *
     * @author : libin.chen
     */
    public int modifyBookInfo(Book book, String userRtx) {

        int modifyFail = 0, modifySuccess = 1;

        Integer bookInfoId = bookDao.getBookInfoIdByBookId(book.getBookId());

        if (bookInfoId == null)
            return modifyFail;

        book.setBookInfoId(bookInfoId.intValue());
        /**
         * 调用 author : lv.jing 判断管理员修改后的图书的大分类或小分类是否在 分类表的这个 图书管 下存在
         * **/
        Integer navId = bookDao.selectBookNavId(book.getTitle(), book.getBookType());
        if (null == navId) { // userRtx 管理员名字
            bookDao.saveNav(book.getTitle(), book.getBookType());
            logService.insertLog(new LogInfo(OperationType.OTHER, new Date(), userRtx, book.getBookLib(), "添加分类成功"));
            navId = bookDao.selectBookNavId(book.getTitle(), book.getBookType());
        }

        if (null == navId) {
            return modifyFail;
        }
        int flag = bookDao.updateBookInfo(book.getBookName(), book.getBookAuthor(), book.getBookPress(),
                book.getBookIntro(), navId, book.getBookInfoId());
        if (flag == 0)
            return modifyFail;
        StringBuilder modifyBookLogInfo = new StringBuilder("修改图书 编号 ： ");
        // 让Lucene更新索引
        logService.insertLog(new LogInfo(OperationType.ALTERBOOK, new Date(), userRtx, book.getBookLib(), modifyBookLogInfo.append(book.getBookId()).append("的图书信息").toString()));
        bookSearchEngine.updateIndex(book);
        return modifySuccess;
    }

    /**
     * @param libId
     * @return
     */
    @Override
    public boolean deleteBookOfLib(int libId) {

        bookDao.deleteBookOfLib(libId, BookStatus.DELETED);
        return true;
    }

    @Override
    public int fetchHotByNavId(int navId) {
        Integer hot;
        hot = bookDao.fetchHotByNavId(navId);
        if (hot == null) {
            hot = 0;
        }
        return hot;
    }

    public void setPageFactory(PageFactory pageFactory) {
        this.pageFactory = pageFactory;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(crawlThreadSize);
    }

    public void setCrawlThreadSize(int crawlThreadSize) {
        this.crawlThreadSize = crawlThreadSize;
    }
}
