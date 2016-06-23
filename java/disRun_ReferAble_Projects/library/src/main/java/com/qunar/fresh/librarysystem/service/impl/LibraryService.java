package com.qunar.fresh.librarysystem.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.dao.LibraryDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.BookService;
import com.qunar.fresh.librarysystem.service.BorrowingService;
import com.qunar.fresh.librarysystem.service.SystemService;
import com.qunar.fresh.librarysystem.utils.RuntimeConstants;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-27 Time: 下午6:38
 * 
 * @author he.chen
 * @author hang.gao
 */
@Service
public class LibraryService {
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    @Resource
    private LibraryDao libraryDao;
    @Resource
    private LogService logService;

    @Resource
    private SystemService systemService;

    @Resource
    private ManagerService managerService;

    @Resource
    private BookService bookService;
    @Resource
    private BorrowingService borrowingService;

    public List<Library> fetchAllLibrarys() {

        return libraryDao.fetchAllLibrarys();
    }

    /**
     * 获取数据库中可用的图书馆的信息
     * 
     * @return
     */
    public Map<String, Object> getAllLibInfo() {
        List<Library> libraryList = fetchAllLibrarys();
        int allLibCount = getCountOfAllLib();
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("total", allLibCount).put("libraryList", libraryList);
        logger.info("得到所有的图书馆信息");
        return jsonResult.getJsonDataMap();
    }

    /**
     * 获取可以用的图书馆的数量
     * 
     * @return
     */
    public int getCountOfAllLib() {
        logger.info("获得总共图书馆数量");
        return libraryDao.libCount(null, null, LibraryStatus.VALID);
    }

    /**
     * 插入一个新的图书馆，并且初始化本图书馆的系统设置表，如果已经存在同部门同名图书馆，则添加失败
     * 
     * @param libDept
     * @param libName
     * @param userRtx
     * @param libId
     * @return
     */
    @Transactional
    public boolean addOneLibrary(String libDept, String libName, String userRtx, int libId) {
        Library library = libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID);
        if (library != null) {
            return false;
        }
        logger.info("增加一个图书馆：部门：{},图书馆名：{}", libDept, libName);

        libraryDao.addOneLibrary(libDept, libName, LibraryStatus.VALID);
        library = libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID);
        systemService.insertSystemData(library.getLibId(), 2, 30, 15, 1, 5);

        StringBuilder logInfo = new StringBuilder();
        logInfo.append("增加一个图书馆: ").append(libName).append(", ").append("所属部门为: ").append(libDept);
        logService.insertLog(OperationType.ADDLIBRARY, new Date(), userRtx, libId,
                logInfo.toString());
        return true;
    }

    /**
     * 从图书馆id获得图书馆的信息
     * 
     * @param libId
     * @return
     */
    public Library fetchLibraryByLibId(int libId) {
        logger.info("通过图书馆id得到图书馆：{}", libId);
        return libraryDao.fetchLibInfo(libId, null, null, LibraryStatus.VALID);
    }

    /**
     * 通过部门和图书馆名得到图书馆
     * 
     * @param libDept
     * @param libName
     * @return
     */
    public Library fetchLibraryByNameDept(String libDept, String libName) {
        logger.info("通过部门和图书馆名得到图书馆：{},{}", libDept, libName);
        return libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID);
    }

    /**
     * 通过图书馆id删除图书馆，并且删除相关的所有图书，系统设置信息 ，管理员信息
     * 
     * @param deletedLibrary
     * @param request
     * @return
     */
    @Transactional
    public boolean deleteLibrary(Library deletedLibrary, HttpServletRequest request) {
        logger.info("通过图书馆id删除图书馆,{}", deletedLibrary.getLibName());

        int deletedLibId = deletedLibrary.getLibId();
        List<Manager> managerList = managerService.fetchAllManager(deletedLibId);
        List<String> rtxs = managerList2RtxArray(managerList);
        int borrowedNum = borrowingService.fetchBorrowedCountOfLib(deletedLibId);
        if (borrowedNum == 0) {
            managerService.deleteManager(rtxs, deletedLibId, request);
            systemService.deleteLibSystemParam(deletedLibId);
            bookService.deleteBookOfLib(deletedLibId);
            libraryDao.deleteLibrary(deletedLibId);
            String userRtx = UserUtils.getUserRtx(request);
            StringBuilder logInfo = new StringBuilder();
            logInfo.append("删除一个图书馆: ").append(deletedLibrary.getLibName()).append("所属部门为：")
                    .append(deletedLibrary.getLibDept());
            logService.insertLog(OperationType.DELETELIBRARY, new Date(), userRtx, deletedLibrary.getLibId(),
                    logInfo.toString());
            return true;
        }
        return false;
    }

    /**
     * 把manager List 转换成 相应的rtx List
     * 
     * @param managerList
     * @return
     */
    private List<String> managerList2RtxArray(List<Manager> managerList) {
        String[] rtxs = null;
        List<String> rtxList = Lists.transform(managerList, new Function<Manager, String>() {
            @Override
            public String apply(Manager input) {

                return input.getUserRtx();
            }
        });
        return rtxList;
    }

    public LibraryDao getLibraryDao() {
        return libraryDao;
    }

    public void setLibraryDao(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public ManagerService getManagerService() {
        return managerService;
    }

    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public BorrowingService getBorrowingService() {
        return borrowingService;
    }

    public void setBorrowingService(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    /**
     * 查询图书馆中的书可以借多久，单位（天）
     * 
     * @param libraryId 图书馆的id
     * @return 图书馆中可借阅的书的天数，如果图书馆不存在，则返回-1，如果图书馆中查询出来的天数为0则返回默认的30天
     * @author hang.gao
     */
    public int selectBorrowPeriod(int libraryId) {
        Preconditions.checkArgument(libraryId > 0);
        Library library = libraryDao.fetchLibInfo(libraryId, null, null, null);
        if (library == null) {
            return -1;
        }
        int day = libraryDao.selectRedecoratePeriod(libraryId);
        return day == 0 ? RuntimeConstants.DEFAULT_BOOK_BORROW_DAY : day;
    }
}
