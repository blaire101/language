package com.qunar.fresh.librarysystem.controller;

import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-8
 * Time: 下午9:11
 * To change this template use File | Settings | File Templates.
 */
public class ManagerControllerTest {
    private ManagerService managerService;
    private LibraryService libraryService;
    private ManagerController managerController;

    @Before
    public void doSetup(){
        managerService = mock(ManagerService.class);
        libraryService = mock(LibraryService.class);
        managerController = new ManagerController();
    }

    @Test
    public void testAddManager_should_return_errcode1_when_userRtx_is_invalid() throws IOException {
        when(UserUtils.checkUser("lll")).thenReturn(false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<String, Object> result =  managerController.addManger("lll", 1, request);
        assertEquals(1,result.get("status"));
    }
    @Test
    public void testDeleteManager(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(UserUtils.getUserRtx(request)).thenReturn("he.chen");
        String[] userRtxs = new String[]{"biwan.yang","qingqing.li"};

    }

}
