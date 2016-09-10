package com.qunar.fresh.librarysystem.controller;

import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.service.BookService;
import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-8
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
public class LibraryControllerTest {
    private LibraryService libraryService;
    private ManagerService managerService;
    private BookService bookService;
    private LogService logService;
    private LibraryController libraryController;

    @Before
    public void doSetup(){
        libraryService = mock(LibraryService.class);
        managerService = mock(ManagerService.class);
        bookService = mock(BookService.class);
        logService = mock(LogService.class);
        libraryController = new LibraryController();
    }
    @Test
    public void testLoadSuperManager(){
        when(libraryService.getAllLibInfo()).thenReturn(new HashMap<String, Object>());
        libraryController.loadSuperManager();
        verify(libraryService).getAllLibInfo();
    }
    @Test
    public void test_loadSuperManager_should_return_errcode1_when_getAllLib_is_null(){
        when(libraryService.getAllLibInfo()).thenReturn(null);
        Map<String, Object> result = libraryController.loadSuperManager();
        assertEquals(1,result.get("status"));

    }

    @Test
    public void test_loadSuperManager_should_return_errcode0_when_getAllLib_is_not_null(){
        when(libraryService.getAllLibInfo()).thenReturn(new HashMap<String, Object>());
        Map<String, Object> result = libraryController.loadSuperManager();
        assertEquals(0,result.get("status"));

    }
    @Test
    public void testViewLibrary_should_return_errcode1_when_param_libId_is_negative(){
        when(managerService.fetchAllManager(-1)).thenReturn(new ArrayList<Manager>());
        when(bookService.getBookCountInOneLib(-1)).thenReturn(1);
        Map<String, Object> result = libraryController.viewLibrary(-1);
        assertEquals(1,result.get("status"));
    }
    @Test
    public void testViewLibrary_should_return_errcode2_when_managerList_is_null(){
        when(managerService.fetchAllManager(1)).thenReturn(null);
        when(bookService.getBookCountInOneLib(1)).thenReturn(1);
        Map<String, Object> result = libraryController.viewLibrary(1);
        assertEquals(2,result.get("status"));
    }
}
