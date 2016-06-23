package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.junit.Assert;
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
public class ExpiresDaoTest {
    @Resource
    private ExpiresDao expiresDao;

    /**
     * @author : libin.chen
     */
    @Test
    public void test_selectValidLibrary() {
        List<Library> listLibrary = expiresDao.selectValidLibrary();
        Assert.assertTrue(listLibrary.size() > 0);
    }

    @Test
    public void test_getReminderExpiresInfo() {
        List<ReminderInfo> list = expiresDao.getReminderExpiresInfo(5);
        Assert.assertTrue(list.size() > 0);
    }
}
