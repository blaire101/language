package com.qunar.fresh.librarysystem.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.LibraryDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import com.qunar.fresh.librarysystem.utils.RuntimeConstants;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-7
 * Time: 下午9:37
 *
 * @author he.chen
 * @author hang.gao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class LibraryServiceTest {

    private LibraryDao libraryDao;
    private LogService logService;
    private LibraryService libraryService;
    private ManagerService managerService;
    private BorrowingService borrowingService;
    @Resource
    private SystemService systemService;

    private static final String RTX_ATTRIBUTE = "userRtx";


    @Before
    public void doSetUp() {
        libraryService = new LibraryService();
        libraryDao = mock(LibraryDao.class);
        libraryService.setLibraryDao(libraryDao);
        logService = mock(LogService.class);
        systemService = mock(SystemService.class);
        libraryService.setLogService(logService);
        managerService = mock(ManagerService.class);
        libraryService.setSystemService(systemService);
        borrowingService = mock(BorrowingService.class);
        libraryService.setManagerService(managerService);
        libraryService.setBorrowingService(borrowingService);
    }

    @Test
    public void testFetchAllLibrarys() {
        when(libraryDao.fetchAllLibrarys()).thenReturn(new ArrayList<Library>());
        libraryService.fetchAllLibrarys();
        verify(libraryDao).fetchAllLibrarys();
    }

    @Test
    public void testGetCountOfAllLib() {
        when(libraryDao.libCount(null, null, LibraryStatus.VALID)).thenReturn(2);
        assertEquals(libraryService.getCountOfAllLib(), 2);
    }

    @Test
    @Ignore
    public void testAddOneLibrary() {
        String userRtx = "he.chen";
        int libId = 1;
        libraryService.addOneLibrary("jiudian", "图书馆", userRtx,libId);
        verify(libraryDao).addOneLibrary("jiudian", "图书馆", LibraryStatus.VALID);
    }

    @Test
    public void test_addOneLibrary_should_return_false_when_library_is_in_db() {
        String libDept = "酒店事业部";
        String libName = "酒店图书馆";
        String userRtx = "he.chen";
        int libId = 1;
        when(libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID)).thenReturn(new Library());
        boolean isSuccess = libraryService.addOneLibrary(libDept, libName, userRtx,libId);
        assertFalse(isSuccess);
    }

    @Test
    public void test_addOneLibrary_should_return_true_when_library_isNot_in_db() {
        String libDept = "酒店事业部";
        String libName = "酒店图书馆";
        String userRtx = "he.chen";
        int libId = 1;
        Library library = mock(Library.class);
        when(libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID)).thenReturn(null).thenReturn(library);
        when(library.getLibId()).thenReturn(1);
        boolean isSuccess = libraryService.addOneLibrary(libDept, libName,userRtx,libId);
        verify(libraryDao).addOneLibrary(libDept, libName, LibraryStatus.VALID);
        verify(systemService).insertSystemData(library.getLibId(), 2, 30, 15, 1, 5);
        assertTrue(isSuccess);
    }

    @Test
    //@Ignore
    public void testDeleteLibrary() {
        Library deletedLib = new Library();
        deletedLib.setLibId(3);
        List<Manager> managerList = new ArrayList<Manager>();
        //List<String> rtxs = new ArrayList<String>();
        HttpServletRequest request = mock(HttpServletRequest.class);
        BookService bookService = mock(BookService.class);
        when(managerService.fetchAllManager(3)).thenReturn(managerList);
        //when(Lists.transform(anyList(), any(Function.class))).thenReturn(rtxs);
        when(borrowingService.fetchBorrowedCountOfLib(3)).thenReturn(0);
        Boolean isSuccess = libraryService.deleteLibrary(deletedLib, request);
        verify(managerService).deleteManager(anyList(), deletedLib.getLibId(), request);
        verify(systemService).deleteLibSystemParam(deletedLib.getLibId());
        verify(bookService).deleteBookOfLib(deletedLib.getLibId());
        verify(libraryDao).deleteLibrary(deletedLib.getLibId());
        assertTrue(isSuccess);
    }

    @Test
    public void test_deleteLibrary() {
        Library library = new Library();
        library.setLibId(3);
        when(managerService.fetchAllManager(3)).thenReturn(new ArrayList<Manager>());
        when(borrowingService.fetchBorrowedCountOfLib(3)).thenReturn(1);
        boolean isAdd = libraryService.deleteLibrary(library, mock(HttpServletRequest.class));
        assertFalse(isAdd);
    }

    /**
     * 测试查询借书时长
     *
     * @author hang.gao
     */
    @Test
    public void testSelectBorrowPeriod() {
        Library library = new Library();
        library.setLibId(1);
        when(libraryDao.fetchLibInfo(1, null, null, null)).thenReturn(library);
        when(libraryDao.selectRedecoratePeriod(1)).thenReturn(10);
        assertEquals(10, libraryService.selectBorrowPeriod(library.getLibId()));
    }

    /**
     * 测试查询借书时长，当数据库中的时长为0天时
     *
     * @author hang.gao
     */
    @Test
    public void testSelectBorrowPeriod_when_period_is_0() {
        Library library = new Library();
        library.setLibId(1);
        when(libraryDao.fetchLibInfo(1, null, null, null)).thenReturn(library);
        when(libraryDao.selectRedecoratePeriod(1)).thenReturn(0);
        assertEquals(RuntimeConstants.DEFAULT_BOOK_BORROW_DAY, libraryService.selectBorrowPeriod(library.getLibId()));
    }

    /**
     * 测试查询借书时长，当图书馆不存在时
     *
     * @author hang.gao
     */
    @Test
    public void testSelectBorrowPeriod_when_library_is_not_exists() {
        Library library = new Library();
        library.setLibId(1);
        when(libraryDao.fetchLibInfo(1, null, null, null)).thenReturn(null);
        when(libraryDao.selectRedecoratePeriod(1)).thenReturn(0);
        assertEquals(-1, libraryService.selectBorrowPeriod(library.getLibId()));
    }

    @Test
    public void test_deleteLibSystemParam() {
        systemService.deleteLibSystemParam(4);
    }

    @Ignore
    @Test
    public void test_addOneLibrary() {
        String libDept = "flight";
        String libName = "tushuguan";
        String userRtx = "he.chen";
        int libId = 1;
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(session.getAttribute(RTX_ATTRIBUTE)).thenReturn("he.chen");
        when(UserUtils.getUserRtx(request)).thenReturn("he.chen");
        when(libraryDao.fetchLibInfo(null, libDept, libName, LibraryStatus.VALID)).thenReturn(null);
        libraryService.addOneLibrary(libDept, libName, userRtx,libId);

    }
}
