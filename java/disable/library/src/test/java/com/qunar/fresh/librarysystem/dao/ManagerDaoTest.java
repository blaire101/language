package com.qunar.fresh.librarysystem.dao;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-16
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class ManagerDaoTest {
    @Resource
    ManagerDao managerDao;

    @Test
    public void test_fetchAllManager(){
        List<Manager> managerList = managerDao.fetchAllManager(5);
        Manager manager = new Manager();
        manager.setUserRtx("libin.chen");
        manager.setLibId(0);
        manager.setStatus(1);
        manager.setSuper(0);

    }

    @Test
    public void test_addManager(){
        Manager manager = new Manager();
        manager.setUserRtx("he.chen");
        manager.setLibId(0);
        manager.setStatus(1);
        manager.setSuper(0);
        managerDao.addManager(manager);
    }
    @Test
    public void test_deleteManager(){
        Manager manager = new Manager();
        manager.setUserRtx("he.chen");
        manager.setLibId(0);
        manager.setStatus(1);
        manager.setSuper(0);
        managerDao.deleteManager(1,Lists.newArrayList(manager));
    }

    @Test
    public void test_selectRole(){
        Manager manager = managerDao.fetchManager("he.chen", AdminUserStatus.ISVALID);
        assertNotNull(manager);

    }
    @Test
    public void test_fetchManager(){
        Manager manager = managerDao.fetchManager("he.chen", AdminUserStatus.ISVALID);
        assertNotNull(manager);
    }
}
