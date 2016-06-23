package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.BorrowingDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.BorrowedBookInfo;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.model.enums.Returned;
import com.qunar.fresh.librarysystem.service.impl.BorrowingServiceImpl;
import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: feiyan.shan
 * Date: 14-4-2
 * Time: 上午10:07
 *
 * @author hang.gao
 */

public class BorrowingServiceTest {
    private Logger logger = LoggerFactory.getLogger(BorrowingServiceTest.class);
    private BorrowingDao borrowingDao;

    private BorrowingServiceImpl borrowingServiceImpl;

    private LibraryService libraryService;

    private BookService bookService;
    private ReserveService reserveService;

    private SystemService systemService;

    private LogService logService;

    @Before
    public void doSetUp() {
        systemService = mock(SystemService.class);
        borrowingDao = mock(BorrowingDao.class);
        libraryService = mock(LibraryService.class);
        bookService = mock(BookService.class);
        reserveService = mock(ReserveService.class);
        logService = mock(LogService.class);
        borrowingServiceImpl = new BorrowingServiceImpl(borrowingDao);
        borrowingServiceImpl.setReserveService(reserveService);
        borrowingServiceImpl.setBookService(bookService);
        borrowingServiceImpl.setLibraryService(libraryService);
        borrowingServiceImpl.setBorrowingDao(borrowingDao);
        borrowingServiceImpl.setSystemService(systemService);
        borrowingServiceImpl.setLogService(logService);
    }

     @Test
    public void testGetBookInfo_should_return_null_when_managerRtx_is_not_valid() {
        String managerRtx = "feiyan";
        String bookId = "B2-01";
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(null);
        Assert.assertNotNull(managerRtx);
        Assert.assertNotNull(bookId);
        Assert.assertNull(borrowingServiceImpl.getBookInfo(managerRtx, bookId));
    }

    @Test
    public void testGetBookInfo_should_get_right_book_info() {
        String managerRtx = "feiyan.shan";
        String bookId = "B2-01";
        Book book = new Book();
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(2);
        when(borrowingDao.selectBookBelongManager(anyInt(), anyString())).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocationOnMock) throws Throwable {
                Book book1 = new Book();
                book1.setBookId(invocationOnMock.getArguments()[1].toString());
                logger.info(invocationOnMock.getArguments()[1].toString());
                return book1;
            }
        });
        Assert.assertTrue(book.getBookId().isEmpty());
        book = borrowingServiceImpl.getBookInfo(managerRtx, bookId);
        Assert.assertEquals(bookId, book.getBookId());
        logger.info(book.getBookId());
    }

     @Test
    public void testGetUserInfo_should_return_null_when_manager_not_in_DB() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(null);
        Assert.assertNull(borrowingServiceImpl.getUserInfo(managerRtx, user));
    }


    @Test
    public void testGetUserInfo_should_get_user_info_when_not_in_DB() {
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        int libId = 2;
        int browTotalNum = 3;
        User user = new User();
        user.setUserRtx(userRtx);
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(null);
        when(systemService.getBorrowNumber(anyInt())).thenReturn(browTotalNum);
        when(borrowingDao.insertUserInfoToUserTable(any(User.class))).thenReturn(1);

        User resultUser = borrowingServiceImpl.getUserInfo(managerRtx, user);
        Assert.assertTrue(resultUser.getLibId()== libId);
        Assert.assertTrue(resultUser.getBorrowNum() == 0);
        Assert.assertTrue(resultUser.getRemainBrowNum() == browTotalNum);
    }

    @Test
    public void testGetUserInfo_should_get_user_info_when_in_DB() {
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        int libId = 2;
        int browTotalNum = 3;
        User user = new User();
        user.setUserRtx(userRtx);
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(2);
        when(borrowingDao.getUserInfo(anyInt(),anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(2);
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(browTotalNum);
        when(borrowingDao.insertUserInfoToUserTable(any(User.class))).thenReturn(1);

        User resultUser = borrowingServiceImpl.getUserInfo(managerRtx, user);
        Assert.assertTrue(resultUser.getLibId()== libId);
        Assert.assertTrue(resultUser.getBorrowNum() == 2);
        Assert.assertTrue(resultUser.getRemainBrowNum() == 1);
    }

    @Test
    public void testBorrowAllBook_should_return_null_when_manager_not_valid() {
        List<String> bookIds = new ArrayList<String>();
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        User user = new User();
        user.setUserRtx(userRtx);

        when(borrowingDao.getManagerLibId(anyString())).thenReturn(null);
        Assert.assertNull(borrowingServiceImpl.borrowAllBook(bookIds, managerRtx, user));
    }

    @Test
    public void testBorrowAllBook_should_return_null_when_user_remain_brow_num_less_than_bookIds_lenth() {
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        // borrowAllBook 参数校验
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);

        // beginToGetUserInfo
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(1);

        //initExistUser
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(1);    //已借阅数
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);  //借阅总数
        Assert.assertNull(borrowingServiceImpl.borrowAllBook(bookIds, managerRtx, user));
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test
    public void testBorrowAllBook_should_insert_bookIds_to_borrowed_when_user_not_in_user_table() {
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        // borrowAllBook 参数校验
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);

        // beginToGetUserInfo
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(1);

        //initExistUser
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(1);    //已借阅数
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);  //借阅总数

        //进入beginToBorrowAllBook
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1); //有效

        //进入borrowOne

        //buildBorrowBookInfo
        when(borrowingDao.selectBookById(anyString())).then(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocationOnMock) throws Throwable {
                Book book = new Book();
                book.setBookId(invocationOnMock.getArguments()[0].toString());
                return book;
            }
        });
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        //回归 borrowOne
        when(borrowingDao.insertBorrowedBook(any(BorrowedBookInfo.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(1);       //成功
        when(borrowingDao.updateUserBorrowNumInfo(anyString(), anyInt(), anyInt())).thenReturn(1);    //成功

        borrowingServiceImpl.borrowAllBook(bookIds, managerRtx, user);
        verify(borrowingDao, times(bookIds.size())).insertBorrowedBook(any(BorrowedBookInfo.class));
        verify(logService,times(3)).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());

    }

    @Test
    public void testBorrowAllBook_should_not_insert_bookIds_to_borrowed_when_manager_has_no_right() {
        List<String> bookIds = new ArrayList<String>();
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        // beginToGetUserInfo
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(1);

        //initExistUser
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(1);    //已借阅数
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);  //借阅总数

        //进入beginToBorrowAllBook
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(null); //无效

        when(borrowingDao.selectBookById(anyString())).thenReturn(new Book());

        List<Book> failedBookList = borrowingServiceImpl.borrowAllBook(bookIds, managerRtx, user);
        verify(borrowingDao, never()).insertBorrowedBook(any(BorrowedBookInfo.class));
        Assert.assertTrue(failedBookList.size() == bookIds.size());
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test(expected = BorrowingServiceImpl.DataConsistencyException.class)
    public void testBorrowAllBook_should_throw_exception_when_update_book_fail() {
        List<String> bookIds = new ArrayList<String>();
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        // beginToGetUserInfo
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(1);

        //initExistUser
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(1);    //已借阅数
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);  //借阅总数

        //进入beginToBorrowAllBook
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1); //有效

        //进入borrowOne

        //buildBorrowBookInfo
        when(borrowingDao.selectBookById(anyString())).then(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocationOnMock) throws Throwable {
                Book book = new Book();
                book.setBookId(invocationOnMock.getArguments()[0].toString());
                return book;
            }
        });
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        //回归 borrowOne
        when(borrowingDao.insertBorrowedBook(any(BorrowedBookInfo.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(0);       //成功
        when(borrowingDao.updateUserBorrowNumInfo(anyString(), anyInt(), anyInt())).thenReturn(1);    //成功
        borrowingServiceImpl.borrowAllBook(bookIds,managerRtx,user);
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test(expected = BorrowingServiceImpl.DataConsistencyException.class)
    public void testBorrowAllBook_should_throw_exception_when_update_user_fail() {
        List<String> bookIds = new ArrayList<String>();
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        // beginToGetUserInfo
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(1);

        //initExistUser
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setBorrowNum(1);    //已借阅数
                return user1;
            }
        });
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);  //借阅总数

        //进入beginToBorrowAllBook
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1); //有效

        //进入borrowOne

        //buildBorrowBookInfo
        when(borrowingDao.selectBookById(anyString())).then(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocationOnMock) throws Throwable {
                Book book = new Book();
                book.setBookId(invocationOnMock.getArguments()[0].toString());
                return book;
            }
        });
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        //回归 borrowOne
        when(borrowingDao.insertBorrowedBook(any(BorrowedBookInfo.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(1);       //成功
        when(borrowingDao.updateUserBorrowNumInfo(anyString(), anyInt(), anyInt())).thenReturn(0);    //成功
        borrowingServiceImpl.borrowAllBook(bookIds,managerRtx,user);
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());

    }

    @Test
    public void testGetUserBorrowedBook_should_return_null_when_manager_not_in_DB() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(null);
        Assert.assertNull(borrowingServiceImpl.getUserBorrowedBook(managerRtx, user));
    }

    @Test
    public void testGetUserBorrowedBook_should_return_default_user_when_user_not_in_DB() {
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        int browTotalNum = 5;

        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(null);
        //initNotExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);

        User resultUser = borrowingServiceImpl.getUserBorrowedBook(managerRtx, user);
        Assert.assertTrue(resultUser.getLibId() == libId);
        Assert.assertTrue(resultUser.getBorrowNum() == 0);
        Assert.assertTrue(resultUser.getRemainBrowNum() == browTotalNum);
    }

    @Test
    public void testGetUserBorrowedBook_should_return_default_user_when_user_has_no_brow_book() {
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        int browTotalNum = 5;

        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(0);
        when(borrowingDao.getUserBorrowedBook(anyInt(), anyString())).thenReturn(null);
        //initNotExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(5);

        User resultUser = borrowingServiceImpl.getUserBorrowedBook(managerRtx, user);
        Assert.assertTrue(resultUser.getLibId() == libId);
        Assert.assertTrue(resultUser.getBorrowNum() == 0);
        Assert.assertTrue(resultUser.getRemainBrowNum() == browTotalNum);
    }


    @Test
    public void testGetUserBorrowedBook_should_return_user_borrowed_books() {
        String managerRtx = "feiyan.shan";
        String userRtx = "libin.chen";
        User user = new User();
        user.setUserRtx(userRtx);
        int libId = 2;
        int browTotalNum = 5;

        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserHasBorrowNum(anyInt(), anyString())).thenReturn(2);
        when(borrowingDao.getUserBorrowedBook(anyInt(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                List<BorrowedBookInfo> borrowedBookInfos = Lists.newArrayList();
                borrowedBookInfos.add(new BorrowedBookInfo());
                borrowedBookInfos.add(new BorrowedBookInfo());
                user1.setResultList(borrowedBookInfos);
                user1.setBorrowNum(2);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(browTotalNum);

        User resultUser = borrowingServiceImpl.getUserBorrowedBook(managerRtx, user);
        Assert.assertTrue(resultUser.getLibId() == libId);
        Assert.assertTrue(resultUser.getBorrowNum() == 2);
        Assert.assertTrue(resultUser.getResultList().size() == 2);
        Assert.assertTrue(resultUser.getRemainBrowNum() == 3);
    }

    @Test
    public void testReturnBookList_should_return_null_when_manager_not_in_DB() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();

        when(borrowingDao.getManagerLibId(anyString())).thenReturn(null);
        Assert.assertNull(borrowingServiceImpl.returnBookList(managerRtx, bookIds, user));
    }

    @Test
    public void testReturnBookList_should_not_return_book_when_has_no_borrow_book() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                user1.setBorrowNum(0);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);
        //beginToReturn
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1);
        //returnOneBook
        borrowingServiceImpl.returnBookList(managerRtx, bookIds, user);
        verify(borrowingDao, never()).updateBorrowedBookIsReturn(anyString(), any(Returned.class));
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }


    @Test
    public void testReturnBookList_should_not_return_right_num_when_has_borrow_book() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                user1.setBorrowNum(3);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);
        //beginToReturn
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1);
        //returnOneBook
        when(borrowingDao.selectBookById(anyString())).thenReturn(new Book());
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        when(borrowingDao.getBorrowedUser(anyString(), any(Returned.class))).thenReturn(userRtx);
        when(borrowingDao.updateBorrowedBookIsReturn(anyString(), any(Returned.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(1);

        when(reserveService.dealReminderReserveUser()).thenReturn(1);
        when(borrowingDao.updateUserBorrowNumInfo(anyString(), anyInt(), anyInt())).thenReturn(1);
        borrowingServiceImpl.returnBookList(managerRtx, bookIds, user);
        verify(borrowingDao, times(3)).updateBorrowedBookIsReturn(anyString(), any(Returned.class));
        verify(logService,times(3)).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test(expected = BorrowingServiceImpl.DataConsistencyException.class)
    public void testReturnBookList_should_throw_DataConsistencyException_when_update_borrowed_fail() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                user1.setBorrowNum(3);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);
        //beginToReturn
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1);
        //returnOneBook
        when(borrowingDao.selectBookById(anyString())).thenReturn(new Book());
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        when(borrowingDao.getBorrowedUser(anyString(), any(Returned.class))).thenReturn(userRtx);
        when(borrowingDao.updateBorrowedBookIsReturn(anyString(), any(Returned.class))).thenReturn(0);
        borrowingServiceImpl.returnBookList(managerRtx, bookIds, user);
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test(expected = BorrowingServiceImpl.DataConsistencyException.class)
    public void testReturnBookList_should_throw_DataConsistencyException_when_update_book_fail() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                user1.setBorrowNum(3);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);
        //beginToReturn
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1);
        //returnOneBook
        when(borrowingDao.selectBookById(anyString())).thenReturn(new Book());
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        when(borrowingDao.getBorrowedUser(anyString(), any(Returned.class))).thenReturn(userRtx);
        when(borrowingDao.updateBorrowedBookIsReturn(anyString(), any(Returned.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(0);
        borrowingServiceImpl.returnBookList(managerRtx, bookIds, user);
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    @Test(expected = BorrowingServiceImpl.DataConsistencyException.class)
    public void testReturnBookList_should_throw_DataConsistencyException_when_update_user_fail() {
        String managerRtx = "sai.liu";
        String userRtx = "shanfeiyan";
        User user = new User();
        user.setUserRtx(userRtx);
        List<String> bookIds = new ArrayList<String>();
        bookIds.add("108");
        bookIds.add("109");
        bookIds.add("110");
        int libId = 2;
        when(borrowingDao.getManagerLibId(anyString())).thenReturn(libId);
        when(borrowingDao.getUserInfo(anyInt(), anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User();
                user1.setLibId(Integer.parseInt(invocationOnMock.getArguments()[0].toString()));
                user1.setUserRtx(invocationOnMock.getArguments()[1].toString());
                user1.setBorrowNum(3);
                return user1;
            }
        });
        //initExistUser
        when(systemService.getBorrowNumber(anyInt())).thenReturn(2);
        //beginToReturn
        when(borrowingDao.checkManagerRight(anyInt(), anyString(), any(BookStatus.class))).thenReturn(1);
        //returnOneBook
        when(borrowingDao.selectBookById(anyString())).thenReturn(new Book());
        when(systemService.getBorrowPeriod(anyInt())).thenReturn(30);
        when(systemService.getRedecBorrowNumber(anyInt())).thenReturn(3);

        when(borrowingDao.getBorrowedUser(anyString(), any(Returned.class))).thenReturn(userRtx);
        when(borrowingDao.updateBorrowedBookIsReturn(anyString(), any(Returned.class))).thenReturn(1);
        when(bookService.modifyBookStatus(anyString(), any(BookStatus.class))).thenReturn(1);

        when(reserveService.dealReminderReserveUser()).thenReturn(1);
        when(borrowingDao.updateUserBorrowNumInfo(anyString(), anyInt(), anyInt())).thenReturn(0);
        borrowingServiceImpl.returnBookList(managerRtx, bookIds, user);
        verify(logService,never()).insertLog(any(OperationType.class),any(Date.class),anyString(),anyInt(),anyString());
    }

    /**
     * 测试续借，当操作员没有管理图书馆时
     *
     * @author hang.gao
     */
    @Test
    public void test_redec_book_when_operator_is_not_admin() {
        BorrowingServiceImpl serviceImpl = new BorrowingServiceImpl();
        Date date = new Date(System.currentTimeMillis());
        serviceImpl.setBorrowingDao(borrowingDao);
        serviceImpl.setLibraryService(libraryService);
        when(borrowingDao.selectBorrowedBookReturnDate("9787111104414")).thenReturn(date);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void test_redec_book_when_book_cannot_redec() {
        Book book = new Book();
        book.setBookId("9787111104414");
        Date date = new Date(System.currentTimeMillis());
        BorrowingServiceImpl serviceImpl = new BorrowingServiceImpl();
        when(bookService.queryBookByBookId("9787111104414")).thenReturn(book);
        when(borrowingDao.selectBorrowedBookReturnDate("9787111104414")).thenReturn(date);
        when(borrowingDao.selectRedecoratedCount("9787111104414")).thenReturn(0);   //不能被续借
        serviceImpl.setBorrowingDao(borrowingDao);
        serviceImpl.setLibraryService(libraryService);
        serviceImpl.setBookService(bookService);
    }

    /**
     * 可以再续借的情况
     *
     * @author hang.gao
     */
    @Test
    public void test_redec_book_when_book_can_redec() {
        Book book = new Book();
        book.setBookId("9787111104414");
        book.setBookLib(1);
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date newDate = new Date(calendar.getTimeInMillis());
        BorrowingServiceImpl serviceImpl = new BorrowingServiceImpl();
        when(borrowingDao.selectBorrowedBookReturnDate("9787111104414")).thenReturn(date);
        when(borrowingDao.selectRedecoratedCount("9787111104414")).thenReturn(1);
        when(libraryService.selectBorrowPeriod(1)).thenReturn(30);
        when(borrowingDao.increaseRedecorateCount("9787111104414", newDate)).thenReturn(1);
        when(bookService.queryBookByBookId("9787111104414")).thenReturn(book);
        serviceImpl.setBorrowingDao(borrowingDao);
        serviceImpl.setLibraryService(libraryService);
        serviceImpl.setBookService(bookService);
    }

    @Test
    public void testRedecBookSuccess() {
        Book book = new Book();
        book.setBookId("aaa");
        book.setBookLib(1);
        borrowingServiceImpl.setBorrowingDao(borrowingDao);
        borrowingServiceImpl.setBookService(bookService);
        when(systemService.getRedecBorrowNumber(1)).thenReturn(100);
        when(borrowingDao.selectRedecoratedCount("aaa")).thenReturn(1);

        java.sql.Date returnDate = new java.sql.Date(System.currentTimeMillis());

        when(borrowingDao.selectBorrowedBookReturnDate("aaa")).thenReturn(returnDate);
        when(borrowingDao.increaseRedecorateCount(anyString(), Mockito.<Date>any())).thenReturn(1);
        when(bookService.queryBookByBookId("aaa")).thenReturn(book);
        when(libraryService.selectBorrowPeriod(1)).thenReturn(30);
        when(borrowingDao.selectBorrowedBookReturnDate("aaa")).thenReturn(new Date(System.currentTimeMillis()));
    }

    /**
     * @libin.chen test  getUserBorrowedBookList
     */
    @Test
    //@Ignore
    public void test_getUserBorrowedBookList_Two_List_size_notEqual() throws IOException {

        List<User> userBorrowedBookList = Lists.newArrayList();
        List<User> userBorrowedOtherInfoList = Lists.newArrayList();

        User user = mock(User.class);

        userBorrowedBookList.add(user);

        String userRtx = new String("libin.chen");

        when(borrowingDao.getUserBorrowedBookList(userRtx)).thenReturn(userBorrowedBookList);

        when(borrowingDao.getUserBorrowedOtherInfo(userRtx)).thenReturn(userBorrowedOtherInfoList);

        Assert.assertEquals(borrowingServiceImpl.getUserBorrowedBookList(userRtx), null);

        verify(borrowingDao).getUserBorrowedBookList(userRtx);
        verify(borrowingDao).getUserBorrowedOtherInfo(userRtx);

    }

    @Test
    //@Ignore
    public void test_getUserBorrowedBookList_Two_List_size_Equal() throws IOException {

        List<User> userBorrowedBookList = Lists.newArrayList();
        List<User> userBorrowedOtherInfoList = Lists.newArrayList();

        String userRtx = new String("libin.chen");

        when(borrowingDao.getUserBorrowedBookList(userRtx)).thenReturn(userBorrowedBookList);

        when(borrowingDao.getUserBorrowedOtherInfo(userRtx)).thenReturn(userBorrowedOtherInfoList);

        Assert.assertEquals(borrowingServiceImpl.getUserBorrowedBookList(userRtx).size(), 0);

        verify(borrowingDao).getUserBorrowedBookList(userRtx);
        verify(borrowingDao).getUserBorrowedOtherInfo(userRtx);

    }
}
