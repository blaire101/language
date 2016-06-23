package com.qunar.fresh.librarysystem.service;

import com.google.common.escape.Escapers;
import com.google.common.primitives.Ints;
import com.google.common.xml.XmlEscapers;
import com.qunar.fresh.librarysystem.dao.ManagerDao;
import com.qunar.fresh.librarysystem.dao.UserDao;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import com.qunar.fresh.librarysystem.service.impl.UserServiceImpl;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-12
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class UserServiceTest {
    @Resource
    private UserServiceImpl userService;
    private UserDao userDao;
    private ManagerDao managerDao;

    @Before
    public void doSetup() {
        userDao = mock(UserDao.class);
        managerDao = mock(ManagerDao.class);

        userService.setUserDao(userDao);
        userService.setManagerDao(managerDao);
    }

    @Test
    public void test_setSession() {
        String name = "hello";
        Date date = new Date();
        System.out.println(Long.toString(new Date().getTime()));
        System.out.println(new Date().getTime());
        String str = Escapers.builder().addEscape('h', "he.chen").build().escape("he.chen");
        str = XmlEscapers.xmlAttributeEscaper().escape("he.chen");
        String s1 = "he.chen";
        str = Base64.encode(s1.getBytes());
        byte[] s2 = Base64.decode(str);
        String newstr = new String(s2);
        int id = 5;
        String encodeStr = Base64.encode(Ints.toByteArray(id));
        int decodeInt = Ints.fromByteArray(Base64.decode(encodeStr));

    }

    @Test
    public void test_setLoginStatus() {
        userService.setLoginStatus(anyString(), anyString());
        verify(userDao).loginUser(anyString(), anyString());
    }

    @Test
    public void test_checkLogin(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie("token","123"),
                                        new Cookie("name","aGUuY2hlbg=="),
                                        new Cookie("userLibId","AAAAAA==")};
        String userRtx = "he.chen";
        when(request.getCookies()).thenReturn(cookies);
        when(userDao.checkUserLogin("123")).thenReturn(userRtx);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(new Manager(userRtx, 1));
        boolean isLogin = userService.checkLogin("123",request);
        assertTrue(isLogin == false);
    }

    @Test
    public void test_getUserLibId_return0_when_userRtx_isNullOrEmpty(){
        int nullRtxLibId = userService.getUserLibId(null);
        assertEquals(0,nullRtxLibId);
        int emptyRtxLibId = userService.getUserLibId(null);
        assertEquals(0,emptyRtxLibId);
    }

    @Test
    public void test_getUserLibId_return1(){
        String userRtx = "he.chen";
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(new Manager(userRtx,1));
        int libId = userService.getUserLibId(userRtx);

        assertEquals(1,libId);
    }

    @Test
    public void test_getUserRole_return_admin(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(0);
        manager.setStatus(1);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String role = userService.getUserRole(userRtx);
        assertEquals("admin",role);
    }

    @Test
    public void test_getUserRole_return_superAdmin(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(1);
        manager.setStatus(1);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String role = userService.getUserRole(userRtx);
        assertEquals("superAdmin",role);
    }
    @Test
    public void test_getUserRole_return_reader(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(1);
        manager.setStatus(0);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String role = userService.getUserRole(userRtx);
        assertEquals("reader",role);

        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(null);
         role = userService.getUserRole(userRtx);
        assertEquals("reader",role);
    }

    @Test
    public void test_getRedirectUrl_should_return_superAdminJsp(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(1);
        manager.setStatus(1);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String url = userService.getRedirectUrl(userRtx);
        assertEquals("/superAdmin.jsp",url);
    }

    @Test
    public void test_getRedirectUrl_should_return_adminJsp(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(0);
        manager.setStatus(1);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String url = userService.getRedirectUrl(userRtx);
        assertEquals("/admin.jsp",url);
    }
    @Test
    public void test_getRedirectUrl_should_return_home(){
        String userRtx = "he.chen";
        Manager manager = new Manager(userRtx,1);
        manager.setSuper(0);
        manager.setStatus(0);
        when(managerDao.fetchManager(userRtx, AdminUserStatus.ISVALID)).thenReturn(manager);
        String url = userService.getRedirectUrl(userRtx);
        assertEquals("/",url);
    }
}
