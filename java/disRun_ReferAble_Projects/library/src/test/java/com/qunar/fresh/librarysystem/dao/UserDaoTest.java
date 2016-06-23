package com.qunar.fresh.librarysystem.dao;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-16
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class UserDaoTest {
    @Resource
    UserDao userDao;

    @Test
    public void test_getUserByRtx(){
        User user = userDao.getUserByRtx("he.chen");
        assertNotNull(user);
    }
    @Test
    public void test_insertUser(){
        User user = new User();
        user.setUserRtx("liande.cai");
        user.setUserName("蔡连德");
        user.setUserDept("技术部");
        userDao.insertUser(Lists.newArrayList(user));
    }

    @Test
    public void test_updateUser(){
        User user = new User();
        user.setUserRtx("liande.cai");
        user.setUserName("蔡连德");
        user.setUserDept("酒店");
        userDao.updateUser(user);
    }

    @Test
    public void test_fetchAllStaffs(){
        List<User> userList = userDao.fetchAllStaffs();
        assertNotNull(userList);
    }

    @Test
    public void test_insertUserAllInfo(){
        User user = new User();
        user.setUserRtx("liande.cai");
        user.setUserName("蔡连德");
        user.setUserDept("酒店");
        userDao.insertUserAllInfo(user);
    }

    @Test
    public void test_loginUser(){
        userDao.loginUser("123456","he.chen");
    }

    @Test
    public void test_checkUserLogin(){
        String userRtx = userDao.checkUserLogin("123456");
        assertEquals("he.chen",userRtx);
    }
}
