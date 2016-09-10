package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.ManagerDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen & hang.gao
 * Date: 14-4-7
 * Time: 下午10:20
 * To change this template use File | Settings | File Templates.
 */
public class ManagerServiceTest {


    private ManagerDao managerDao;

    private ManagerService managerService;
    private HttpServletRequest request;
    private LibraryService libraryService;
    private LogService logService;
    @Before
    public void doSetup(){
        managerDao = mock(ManagerDao.class);
        managerService = new ManagerService();
        managerService.setManagerDao(managerDao);
        request = mock(HttpServletRequest.class);
        libraryService = mock(LibraryService.class);
        managerService.setLibraryService(libraryService);
        logService = mock(LogService.class);
        managerService.setLogService(logService);
    }

    @Test
    public void testFetchAllManager(){
        when(managerDao.fetchAllManager(anyInt())).thenReturn(new ArrayList<Manager>());
        managerService.fetchAllManager(anyInt());
        verify(managerDao).fetchAllManager(anyInt());
    }

    @Test
    @Ignore
    public void testAddManager(){
        Manager manager = new Manager();
        String userRtx = "he.chen";
        managerService.addManager(manager,userRtx);
        verify(managerDao).addManager(manager);
    }
    @Test
    public void test_addManager_when_manager_is_delete(){
        Manager manager = new Manager();
        String userRtx = "he.chen";
        manager.setUserRtx(userRtx);
        manager.setLibId(2);
        Manager existManager = new Manager("he.chen",1);
        existManager.setStatus(AdminUserStatus.NOTVALID.code());
        Cookie[] cookies = new Cookie[]{new Cookie("token","123"),
                new Cookie("name","aGUuY2hlbg=="),
                new Cookie("userLibId","AAAAAA==")};
        when(request.getCookies()).thenReturn(cookies);
        when(managerDao.fetchManager(manager.getUserRtx(), null)).thenReturn(existManager);
        Library library = new Library();
        library.setLibName("jishubu");
        when(libraryService.fetchLibraryByLibId(2)).thenReturn(library);
        boolean isAdd = managerService.addManager(manager,userRtx);
        assertTrue(isAdd);
    }

    @Test
    @Ignore
    public void testDeletManger(){
        List<String> userRtxs = Lists.newArrayList("he.chen");
        List<Manager> managerList =Lists.newArrayList();

        managerService.deleteManager(userRtxs,anyInt(),mock(HttpServletRequest.class));
    }


}




