package com.qunar.fresh.librarysystem.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.google.common.base.*;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.*;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import com.qunar.fresh.librarysystem.service.*;

import org.apache.ibatis.session.RowBounds;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.qunar.fresh.librarysystem.dao.BorrowingDao;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.model.enums.Returned;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-28 Time: 上午11:56 To
 *
 * @author feiyan.shan
 * @author libin.chen
 * @author hang.gao
 */
@Service
public class BorrowingServiceImpl implements BorrowingService {

    /**
     * 系统设置不存在时，如：systemService.getRedecBorrowNumber返回null时的默认值
     */
    private static final Integer SYSTEM_PARAM_NOT_EXIST = Integer.valueOf(-1);

    private Logger logger = LoggerFactory.getLogger("servicelogger");

    @Resource
    private BorrowingDao borrowingDao;

    @Resource
    private LibraryService libraryService;
    /**
     * 访问图书服务
     */
    @Resource
    private BookService bookService;

    @Resource
    private ReserveService reserveService;

    /**
     * 系统设置服务
     */
    @Resource
    private SystemService systemService;

    @Resource
    private LogService logService;

    public BorrowingServiceImpl() {

    }

    public BorrowingServiceImpl(BorrowingDao borrowingDao) {
        this.borrowingDao = borrowingDao;
    }

    public void setBorrowingDao(BorrowingDao borrowingDao) {
        this.borrowingDao = borrowingDao;
    }

    public void setReserveService(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Override
    public BookRedecResult redecBook(String bookId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bookId));
        Book book = bookService.queryBookByBookId(bookId);
        if (book == null) {
            return BookRedecResult.NO_SUTCH_BOOK;
        }
        Integer redecCount = borrowingDao.selectRedecoratedCount(bookId);
        if (redecCount == null) {
            return BookRedecResult.BOOK_NOT_BE_BOWRROWED;
        }
        if (Optional.fromNullable(systemService.getRedecBorrowNumber(book.getBookLib())).or(SYSTEM_PARAM_NOT_EXIST) <= redecCount) {
            //不能再续借了
            return BookRedecResult.CANNOT_REDEC_SUTCH_BOOK;
        }
        java.sql.Date newReturnDate = calcuNewReturnDate(book, borrowingDao.selectBorrowedBookReturnDate(bookId));
        return borrowingDao.increaseRedecorateCount(bookId, newReturnDate) == 0 ? BookRedecResult.CANNOT_REDEC_SUTCH_BOOK : new BookRedecResult(newReturnDate, BookRedecResult.SUCCESS_CODE, "");
    }

    private java.sql.Date calcuNewReturnDate(Book book, java.sql.Date returnDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(returnDate.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, libraryService.selectBorrowPeriod(book.getBookLib()));
        java.sql.Date newReturnDate = new java.sql.Date(calendar.getTimeInMillis());
        return newReturnDate;
    }

    /**
     * 初始化数据库中不存在的用户信息（没有借过书，所以没有记录）：总共借阅数，已经借阅数，还可借阅数
     *
     * @param user
     * @param libId
     */
    private void initNotExistUser(User user, int libId) {
        user.setBorrowNum(0);
        int totalBorrowNum = systemService.getBorrowNumber(libId);
        if (totalBorrowNum == BorrowingServiceImpl.SYSTEM_PARAM_NOT_EXIST) {
            throw new DataConsistencyException("没有查询到图书馆的系统设置信息");
        }
        user.setBrowTotalNum(totalBorrowNum);
        user.setRemainBrowNum(user.getBrowTotalNum());
        user.setLibId(libId);
    }

    /**
     * 初始化数据库中存在的用户信息（借过书，所以记录）：总共可借阅数，已经借阅数，还可以借阅数
     *
     * @param libId
     * @param userFinal
     */
    private void initExistUser(Integer libId, User userFinal) {
        int totalBorrowNum = systemService.getBorrowNumber(libId);
        if (totalBorrowNum == BorrowingServiceImpl.SYSTEM_PARAM_NOT_EXIST) {
            throw new DataConsistencyException("没有查询到图书馆的系统设置信息");
        }
        if (userFinal.getBorrowNum() < 0) {
            int borrowNum = borrowingDao.countUserBorrowNumInBorrowedTable(libId, userFinal.getUserRtx());
            userFinal.setBorrowNum(borrowNum);
            setUserBorrowNum(libId, userFinal.getUserRtx(), borrowNum);
        }
        int remainBrowNum = totalBorrowNum - userFinal.getBorrowNum();
        if (remainBrowNum < 0) {
            remainBrowNum = 0;
        }
        userFinal.setRemainBrowNum(remainBrowNum);
        userFinal.setBrowTotalNum(totalBorrowNum);
        userFinal.setLibId(libId);
    }


    /**
     * 根据书籍id查询书籍的详细信息。 使用场合：管理员进入借书界面，并且输入书籍id点确定后，下方展示书籍详细信息
     * 注意：只能返回管理员所管理的图书馆中，书籍的信息。如果该书籍不在管理员的图书馆中，则返回空对象。
     *
     * @param bookId
     * @return
     */
    public Book getBookInfo(String managerRtx, String bookId) {
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效", managerRtx);
            return null;
        }
        return borrowingDao.selectBookBelongManager(libId, bookId.trim()); // 会不会返回null
    }

    /**
     * 根据读者rtx，返回读者信息，包括读者所属部门，已借阅数，剩余借阅数。 但不包括借阅书籍。 注意：只能返回管理员所管理图书馆中，读者的信息。
     * 使用场合：管理员进入借书界面，并且输入读者rtx后，下方展示读者的信息。
     *
     * @return
     */
    public User getUserInfo(final String managerRtx, User user) {
        // 查询数据库，判断管理员和用户是否在数据库中。
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效", managerRtx);
            return null;
        }
        return beginToGetUserInfo(libId, user);
    }

    /**
     * 获取用户信息,不包括借阅书籍列表
     *
     * @param libId
     * @param user
     * @return
     */
    private User beginToGetUserInfo(int libId, User user) {
        User userFilal = user;
        if (null == borrowingDao.getUserHasBorrowNum(libId, user.getUserRtx())) {
            initNotExistUser(user, libId);
            borrowingDao.insertUserInfoToUserTable(user);
            return userFilal;
        } else {
            User tempUser = borrowingDao.getUserInfo(libId, userFilal.getUserRtx());
            userFilal.setBorrowNum(tempUser.getBorrowNum());
            userFilal.setRedecorateTotalNum(tempUser.getRedecorateTotalNum());
            userFilal.setResultList(tempUser.getResultList());
            initExistUser(libId, userFilal);
            return userFilal; //是否需要设置其他一些属性
        }
    }

    /**
     * 将所有借阅书籍列表插入到数据库中 使用场合：管理员进入借书页面，并且输入了所借书籍和读者信息后，点击确定。
     * 注意：管理员只能操作自己所管理的图书馆。
     * <p/>
     * 该函数主要做参数检查
     *
     * @param bookIds 所有书籍的id
     * @return 借阅失败的书籍id。后期需要根据列表来生成不同的json。当列表长度为0时，返回true。否则，返回false，以及该列表信息
     */
    public List<Book> borrowAllBook(List<String> bookIds,
                                    String managerRtx, User userInit) {
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效", managerRtx);
            return null;
        }
        User userFinal = beginToGetUserInfo(libId, userInit);
        if (userFinal.getRemainBrowNum() < bookIds.size()) {
            logger.info("借书列表大于用户可借阅书籍数");
            return null;
        }
        return beginToBorrowAllBook(bookIds, userFinal, managerRtx, libId);
    }


    /**
     * 将所有借阅书籍列表插入到数据库中 使用场合：管理员进入借书页面，并且输入了所借书籍和读者信息后，点击确定。
     * 注意：管理员只能操作自己所管理的图书馆。
     *
     * @param bookIds 所有书籍的id
     * @return 借阅失败的书籍id。后期需要根据列表来生成不同的json。当列表长度为0时，返回true。否则，返回false，以及该列表信息
     */
    @Transactional
    private List<Book> beginToBorrowAllBook(List<String> bookIds, User user, String managerRtx, int libId) {
        List<String> failedBookIds = Lists.newArrayList();
        for (String bookId : bookIds) {
            bookId = CharMatcher.WHITESPACE.removeFrom(bookId);
            bookId = bookId.toUpperCase();
            CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,bookId);
            if (null == borrowingDao.checkManagerRight(libId, bookId, BookStatus.INLIBRARY)) {
                logger.info("管理员 {} 没有权限操作书籍 {} ", managerRtx, bookId);
                failedBookIds.add(bookId);
            } else {
                borrowOneBook(user, managerRtx, libId, bookId);
            }
        }
        return getBookInfo(failedBookIds);
    }

    /**
     * 借阅一本书籍，前提为该书籍在管理员的图书馆中，且没有被借出
     *
     * @param user
     * @param managerRtx
     * @param libId
     * @param bookId
     */
    private void borrowOneBook(User user, String managerRtx, int libId, String bookId) {
        BorrowedBookInfo borrowedBookInfo = buildBorrowBookInfo(libId, bookId, user.getUserRtx());
        borrowingDao.insertBorrowedBook(borrowedBookInfo);
        if (bookService.modifyBookStatus(bookId, BookStatus.BORROWED) == 0) {
            logger.error("借书时，更新book表失败，书籍编号是 {}", bookId);
            throw new DataConsistencyException("借书时，数据库数据不一致：借书前查询book表中该书在库，借书时，更新book表时，该书不在库");
        }
        user.setBorrowNum(user.getBorrowNum() + 1);
        setUserBorrowNum(libId, user.getUserRtx(), user.getBorrowNum());
        insertLogInfo(OperationType.BORROWBOOK, new Date(), managerRtx, libId, borrowedBookInfo);
    }

    /**
     * 通过书籍id，获取书籍的详细信息。
     *
     * @param failedBookIds
     * @return
     */
    private List<Book> getBookInfo(List<String> failedBookIds) {
        List<Book> failedBookInfos = Lists.newArrayList();
        for (String bookId : failedBookIds) {
            Book tempBook = borrowingDao.selectBookById(bookId);
            if (tempBook == null) {
                tempBook = new Book();
                tempBook.setBookId(bookId);
            }
            failedBookInfos.add(tempBook);
        }
        return failedBookInfos;
    }

    /**
     * 借还书记录日志函数。根据操作类型，记录不同操作信息
     *
     * @param operationType
     * @param date
     * @param managerRtx
     * @param libId
     * @param borrowedBookInfo
     */
    private void insertLogInfo(OperationType operationType, Date date, String managerRtx, int libId, BorrowedBookInfo borrowedBookInfo) {
        String operationInfo = "";
        if (operationType.equals(OperationType.RETURNBOOK)) {
            operationInfo = "归还";
        } else {
            operationInfo = "借出";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(borrowedBookInfo.getUserRtx())
                .append(" ")
                .append(operationInfo).append(" 书籍《")
                .append(borrowedBookInfo.getBookName())
                .append("》，书籍编号：")
                .append(borrowedBookInfo.getBookId());
        logService.insertLog(operationType, date, managerRtx, libId, stringBuilder.toString());
    }

    /**
     * 根据用户rtx，查询出用户在管理员所管理的图书馆中，所借书籍。 使用场合：管理员进入还书页面，输入用户rtx后，下面展示用户所借书籍的详细信息。
     *
     * @param managerRtx
     * @return 返回用户详细信息：包括用户所借书籍列表
     */
    public User getUserBorrowedBook(String managerRtx, User userInit) {
        // 查询数据库，判断管理员和用户是否在数据库中。
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效", managerRtx);
            return null;
        }
        if (borrowingDao.getUserHasBorrowNum(libId, userInit.getUserRtx()) == null) {
            initNotExistUser(userInit, libId);
            return userInit;
        }
        User user = borrowingDao.getUserBorrowedBook(libId, userInit.getUserRtx());
        if (user == null) {
            initNotExistUser(userInit, libId);
            return userInit;
        } else {
            userInit.setBorrowNum(user.getBorrowNum());
            userInit.setRedecorateTotalNum(user.getRedecorateTotalNum());
            userInit.setResultList(user.getResultList());
            initExistUser(libId, userInit);
            return userInit;
        }
    }

    /**
     * 根据书籍id和用户rtx，生成用户借阅书籍信息。
     *
     * @param bookId
     * @param userRtx
     * @return
     */
    private BorrowedBookInfo buildBorrowBookInfo(int libId, String bookId, String userRtx) {
        BorrowedBookInfo borrowedBookInfo = new BorrowedBookInfo();
        Book book = borrowingDao.selectBookById(bookId);
        bookService.updateBookHot(book.getBookInfoId());
        borrowedBookInfo.setBookInfo(book);
        Date borrowDate = new Date();
        DateTime dateTime = new DateTime(borrowDate);
        int borrowPeriod = systemService.getBorrowPeriod(libId);
        int redecorateNum = 0;
        if (borrowPeriod == BorrowingServiceImpl.SYSTEM_PARAM_NOT_EXIST || redecorateNum == BorrowingServiceImpl.SYSTEM_PARAM_NOT_EXIST) {
            throw new DataConsistencyException("读取系统设置表出错，没有相关图书馆的借阅设置");
        }
        Date returnDate = dateTime.plusDays(borrowPeriod).toDate();// 需要从SystemParamService获取信息
        borrowedBookInfo.setBorrowedDate(borrowDate);
        borrowedBookInfo.setReturnDate(returnDate);
        borrowedBookInfo.setReturned(Returned.NOTRETURNED);
        borrowedBookInfo.setUserRtx(userRtx);
        borrowedBookInfo.setRedecorateNum(redecorateNum); // 需要从SystemParamService获取

        return borrowedBookInfo;
    }

    /**
     * 实现还书功能。根据参数列表中书籍id和用户rtx更新相关表格。 使用场合：管理员进入还书页面，且选中待还书籍，点击确定后。 该函数主要做参数检查
     *
     * @param managerRtx
     * @param bookIds
     * @param user
     * @return 没有还成功的书籍id列表：根据该列表生成不同的json。如果列表长度为0，则返回true；否则返回false，以及该列表信息
     */
    public List<Book> returnBookList(String managerRtx, List<String> bookIds,
                                     User user) {
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效 ", managerRtx);
            return null;
        }
        user = beginToGetUserInfo(libId, user);
        return beginToReturnBookList(managerRtx, libId, bookIds, user);
    }

    /**
     * 实现还书功能。根据参数列表中书籍id和用户rtx更新相关表格。 使用场合：管理员进入还书页面，且选中待还书籍，点击确定后。
     *
     * @param managerRtx
     * @param bookIds
     * @param user
     * @return 没有还成功的书籍id列表：根据该列表生成不同的json。如果列表长度为0，则返回true；否则返回false，以及该列表信息
     */

    private List<Book> beginToReturnBookList(String managerRtx, int libId,
                                             List<String> bookIds, User user) {
        List<Book> failedId = new ArrayList<Book>();
        for (String bookId : bookIds) {
            bookId = CharMatcher.WHITESPACE.removeFrom(bookId);
            bookId = bookId.toUpperCase();
            if (null == borrowingDao.checkManagerRight(libId, bookId, BookStatus.BORROWED)) {
                logger.error("还书错误：书籍 {} 已经在图书馆中删除。书籍必须为借出状态，且属于管理员图书馆", bookId);
                failedId.add(borrowingDao.selectBookById(bookId));
            } else {
                returnOneBook(managerRtx, libId, user, bookId);
            }
        }
        reserveService.dealReminderReserveUser();
        return failedId;
    }

    /**
     * 归还一本书籍。
     * 前提为该书为管理员所在图书馆，且已被借出
     *
     * @param managerRtx
     * @param libId
     * @param user
     * @param bookId
     * @return
     */
    @Transactional
    private void returnOneBook(String managerRtx, int libId, User user, String bookId) {
        BorrowedBookInfo borrowedBookInfo = buildBorrowBookInfo(libId, bookId,
                user.getUserRtx());

        if (user.getBorrowNum() > 0) {
            String borrowUserRtx = borrowingDao.getBorrowedUser(bookId, Returned.NOTRETURNED);
            if (borrowUserRtx != null && borrowUserRtx.equals(user.getUserRtx())) {
                if (0 == borrowingDao.updateBorrowedBookIsReturn(bookId,
                        Returned.ISRETURNED)) {
                    throw new DataConsistencyException("还书错误：更新borrowed表失败。未知错误");
                } else {
                    user.setBorrowNum(user.getBorrowNum() - 1);
                    // 将book表中的字段修改
                    if (bookService.modifyBookStatus(bookId, BookStatus.INLIBRARY) == 0) {
                        throw new DataConsistencyException("还书错误：更新book表失败.未知错误");
                    }
                    setUserBorrowNum(libId, user.getUserRtx(), user.getBorrowNum());
                    //将管理员操作加入到log中
                    insertLogInfo(OperationType.RETURNBOOK, new Date(), managerRtx, libId, borrowedBookInfo);
                }
            }
        }
    }

    /**
     * 修改用户已借书籍和可借书籍数 //应该为private 使用场合，当用户还书成功后，修改user表中的已借书籍数和可借书籍数
     *
     * @param managerLibId 管理员rtx
     * @param userRtx      用户rtx
     * @param borrowNum    归还数量
     */
    private void setUserBorrowNum(int managerLibId,
                                  String userRtx, int borrowNum) {
        if (0 == borrowingDao.updateUserBorrowNumInfo(userRtx, borrowNum, managerLibId)) {
            // 更新借阅表失败。
            logger.error("更新用户表失败：未将用户的已借阅数和尚可借阅数修改");
            throw new DataConsistencyException("更新用户表失败：未将用户的已借阅数和尚可借阅数修改");
        }
    }

    private List<BorrowedBookInfo> getAllBorrowedBook(String managerRtx,
                                                      int offset, int limit) {
        Integer libId = borrowingDao.getManagerLibId(managerRtx);
        if (libId == null) {
            logger.info("管理员 {} 无效", managerRtx);
            return null;
        }
        List<BorrowedBookInfo> borrowedBookInfos = borrowingDao.getAllBorrowedBook(
                Returned.NOTRETURNED, managerRtx, new RowBounds(offset, limit));
        setViewBorrowedBookInfo(libId, borrowedBookInfos);
        return borrowedBookInfos;
    }

    private void setViewBorrowedBookInfo(int libId, List<BorrowedBookInfo> borrowedBookInfos) {
        int redecorateNum = systemService.getRedecBorrowNumber(libId);
        if (redecorateNum == BorrowingServiceImpl.SYSTEM_PARAM_NOT_EXIST) {
            throw new DataConsistencyException("查询系统设置表可续借次数失败！");
        }
        for (BorrowedBookInfo borrowedBookInfo : borrowedBookInfos) {
            borrowedBookInfo.setRedecorateNum(redecorateNum - borrowedBookInfo.getRedecorateNum());
        }
    }


    /**
     * 获取指定用户的借阅书籍信息。分页查询
     * //     * 以不同图书馆区分。比如用户在图书馆1中的借阅书籍为一个user。用户在图书馆2中借阅的书籍为另一个user
     *
     * @param userRtx 用户Rtx
     * @return
     * @author : libin.chen
     */
    public List<User> getUserBorrowedBookList(String userRtx) {
        List<User> userBorrowedBookList = borrowingDao.getUserBorrowedBookList(userRtx);
        List<User> userBorrowedOtherInfo = borrowingDao.getUserBorrowedOtherInfo(userRtx);
        int BorrowedBookListSize = userBorrowedBookList.size();
        int BorrowedOtherInfoSize = userBorrowedOtherInfo.size();
        if (BorrowedBookListSize != BorrowedOtherInfoSize) {
            return null;
        }
        for (int i = 0; i < BorrowedBookListSize; i++) {
            userBorrowedBookList.get(i).setBrowTotalNum(userBorrowedOtherInfo.get(i).getBrowTotalNum());
            userBorrowedBookList.get(i).setLibDept(userBorrowedOtherInfo.get(i).getLibDept());
            userBorrowedBookList.get(i).setLibName(userBorrowedOtherInfo.get(i).getLibName());
            userBorrowedBookList.get(i).setRedecorateTotalNum(userBorrowedOtherInfo.get(i).getRedecorateTotalNum());

            int remainBrowNum = userBorrowedBookList.get(i).getBrowTotalNum() - userBorrowedBookList.get(i).getBorrowNum();
            userBorrowedBookList.get(i).setRemainBrowNum(remainBrowNum <= 0 ? 0 : remainBrowNum);
            for (BorrowedBookInfo borrowedBookInfo : userBorrowedBookList.get(i).getResultList()) {
                int redecorateNum = userBorrowedBookList.get(i).getRedecorateTotalNum() - borrowedBookInfo.getRedecorateNum();
                redecorateNum = ((redecorateNum <= 0) ? 0 : redecorateNum);
                borrowedBookInfo.setRedecorateNum(redecorateNum);
            }
        }
        return userBorrowedBookList;
    }

    public static class DataConsistencyException extends DataAccessException {
        public DataConsistencyException(String message) {
            super(message);
        }
    }

    @Override
    public int fetchBorrowedCountOfLib(String managerRtx) {
        return borrowingDao.fetchBorrowedCountOfLib(managerRtx, null);
    }


    /**
     * 查看一个图书馆是否有被借阅的书
     *
     * @param libId
     * @return
     * @author he.chen
     */
    @Override
    public int fetchBorrowedCountOfLib(int libId) {
        return borrowingDao.fetchBorrowedCountOfLib(null, libId);
    }


    /**
     * 管理员查看所有借阅信息。
     *
     * @param managerRtx
     * @return 借阅表中尚未归还书籍的
     */
    @Override
    public BorrowedInfo getBorrowedInfo(String managerRtx, int goPageNum, int pageSize) {
        int offset = (goPageNum - 1) * pageSize;
        int limit = pageSize;
        List<BorrowedBookInfo> borrowedBookInfos = getAllBorrowedBook(managerRtx, offset, limit);
        int totalCount = fetchBorrowedCountOfLib(managerRtx);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        BorrowedInfo borrowedInfo = new BorrowedInfo();
        borrowedInfo.setCurrentPageNum(goPageNum);
        borrowedInfo.setPageSize(pageSize);
        borrowedInfo.setResultList(borrowedBookInfos);
        borrowedInfo.setTotalCount(totalCount);
        borrowedInfo.setTotalPage(totalPage);
        return borrowedInfo;
    }
}

