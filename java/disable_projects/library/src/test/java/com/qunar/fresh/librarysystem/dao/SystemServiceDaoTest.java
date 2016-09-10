package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.SystemParam;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yingnan.zhang
 * Date: 14-4-3
 * Time: 下午8:51
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class SystemServiceDaoTest {
    @Resource
    private SystemServiceDao systemServiceDao;
    private SystemParam systemParam = new SystemParam();

    @Test
    public void test_fetchSystemData() {
        Integer Lib_id = 5;
        systemParam.setBorrowPeriod(20);
        systemParam.setBorrowTotalNum(5);
        systemParam.setRedecorateNum(10);
        systemParam.setRedecoratePeriod(10);
        systemParam.setRemindDay(10);
        Assert.assertTrue((systemServiceDao.fetchSystemData(Lib_id)).equals(systemParam));
    }

    @Test
    public void test_fetchSystemCount() {
        Assert.assertTrue(systemServiceDao.fetchSystemCount(5) == 1);
    }

    @Test
    public void test_fetchAdminls() {
        List<Integer> checkObjectMap;
        Map<String, Object> objectMap;
        checkObjectMap = systemServiceDao.fetchAdminls("yayi");
        Assert.assertNull(checkObjectMap);
        checkObjectMap = systemServiceDao.fetchAdminls("yaya");
        Assert.assertNotNull(checkObjectMap);
        Assert.assertTrue(checkObjectMap.size() == 1);
        Assert.assertTrue(checkObjectMap.iterator().next() == 5);//获取该管理员对应的lib_id
    }

    @Test
    public void test_insertLog() {
        Date date = new Date();
        Timestamp NowDate = new Timestamp(date.getTime());
        String info = "系统设置表进行了插入操作";
        Assert.assertTrue(systemServiceDao.insertLog(4, NowDate, "yaya", 5, info) == 1);
    }

    @Test
    public void test_insertSystemData() {
        int Lib_id = 10, borrowTotalNum = 5, borrowPeriod = 20,
                redecoratePeriod = 10, redecorateNum = 10,
                remindDay = 10;
        int checkCount = systemServiceDao.fetchSystemCount(10);
        systemServiceDao.insertSystemData(Lib_id, borrowTotalNum, borrowPeriod, redecoratePeriod,
                redecorateNum, remindDay, 1);
        Assert.assertTrue(systemServiceDao.fetchSystemCount(10) == (checkCount + 1));

    }

    @Test
    public void test_updatSystemData() {
        int Lib_id = 5, borrowTotalNum = 5, borrowPeriod = 20,
                redecoratePeriod = 10, redecorateNum = 10,
                remindDay = 10;
        Assert.assertTrue(systemServiceDao.updateSystemData(Lib_id, borrowTotalNum, borrowPeriod, redecoratePeriod,
                redecorateNum, remindDay) == 1);
        Lib_id = 20;
        Assert.assertTrue(systemServiceDao.updateSystemData(Lib_id, borrowTotalNum, borrowPeriod, redecoratePeriod,
                redecorateNum, remindDay) == 0);
    }

    @Test
    public void test_getBorrowNumber() {
        int Lib_id = 12;
        Assert.assertTrue(systemServiceDao.getBorrowNumber(Lib_id) == null);
        Lib_id = 10;
        Assert.assertTrue(systemServiceDao.getBorrowNumber(Lib_id) == 1);
    }

    @Test
    public void test_getBorrowPeriod() {
        int Lib_id = 12;
        Assert.assertTrue(systemServiceDao.getBorrowPeriod(Lib_id) == null);
        Lib_id = 0;
        Assert.assertTrue(systemServiceDao.getBorrowPeriod(Lib_id) == 2);
    }

    @Test
    public void test_getRedecBorrowPeriod() {
        int Lib_id = 12;
        Assert.assertTrue(systemServiceDao.getRedecBorrowPeriod(Lib_id) == null);
        Lib_id = 0;
        Assert.assertTrue(systemServiceDao.getRedecBorrowPeriod(Lib_id) == 4);
    }

    @Test
    public void test_getRedecBorrowNumber() {
        int Lib_id = 12;
        Assert.assertTrue(systemServiceDao.getRedecBorrowNum(Lib_id) == null);
        Lib_id = 10;
        Assert.assertTrue(systemServiceDao.getRedecBorrowNum(Lib_id) == 10);
    }
}
