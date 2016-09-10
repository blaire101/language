package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.ReminderInfo;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: libin.chen
 * Date: 14-4-5
 * Time: 下午12:37
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class ReserveDaoTest {
    @Resource
    private ReserveDao reserveDao;

    /**
     * @author : libin.chen
     */

    @Test
    public void test_isConExistBookInfoIdAndLibId() {
        int count = reserveDao.isConExistBookInfoIdAndLibId(210, 5);
        org.junit.Assert.assertTrue(count == 0);
    }
    @Test
    public void test_isStillExistInLib_exist_in_Lib() {
        int count = reserveDao.isStillExistInLib(9, 5);
        org.junit.Assert.assertTrue(count > 0);
    }

    @Test
    public void test_addReserveInfo_not_exist_in_Lib() {
        int count = reserveDao.isStillExistInLib(1, 5);
        org.junit.Assert.assertTrue(count == 0);
    }

    @Test
    public void test_isReserved() {
        int count = reserveDao.isReserved("libin.chen", 1, 5);
        org.junit.Assert.assertTrue(count > 0);
    }

    @Test
    public void test_not_isReserved() {
        int count = reserveDao.isReserved("libin.chen", 1, 5);
        org.junit.Assert.assertTrue(count == 0);
    }

    @Test
    public void test_addReserveInfo() {
        int count = reserveDao.addReserveInfo(9, "yingnan.zhang", 5);
        org.junit.Assert.assertTrue(count == 1);
    }

    @Test
    public void test_selectReminderReserveInfo_list_have_data() {
        List<ReminderInfo> list = reserveDao.selectReminderReserveInfo();
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void test_selectReminderReserveInfo_return_empty_list() {
        List<ReminderInfo> list = reserveDao.selectReminderReserveInfo();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void test_updateReserve() {
        int count = reserveDao.updateReserve(24);
        org.junit.Assert.assertTrue(count == 1);
    }
}
