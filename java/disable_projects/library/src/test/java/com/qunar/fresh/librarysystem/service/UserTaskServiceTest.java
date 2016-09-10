package com.qunar.fresh.librarysystem.service;

import com.qunar.fresh.librarysystem.dao.UserDao;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.service.impl.UserTaskService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-15
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class UserTaskServiceTest {
    @Resource
    UserDao userDao;
    @Resource
    UserTaskService userTaskService;

    @Before
    public void doSetup(){
    }
    @Test
    public void test_updateStaffs() throws IOException {
       // userTaskService.updateStaff();
    }

    @Test
    @Ignore
    public void test_updateUserList(){
        when(userDao.fetchAllStaffs()).thenReturn(new ArrayList<User>());
    }
}
