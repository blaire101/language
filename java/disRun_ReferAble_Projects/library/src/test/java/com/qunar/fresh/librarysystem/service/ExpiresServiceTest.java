package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.ExpiresDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: libin.chen
 * Date: 14-4-07
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class ExpiresServiceTest {

    @Resource
    private ExpiresService expiresService;

    @Test
    //@Ignore
    public void test_getReminderExpiresInfo_selectValidLibrary_is_empty() throws IOException {

        ExpiresDao expiresDao = mock(ExpiresDao.class);

        expiresService.setExpiresDao(expiresDao);

        List<ReminderInfo> listReminder = Lists.newArrayList();

        List<Library> listLibrary = Lists.newArrayList();

        when(expiresDao.selectValidLibrary()).thenReturn(listLibrary);

        Assert.assertEquals(expiresService.getReminderExpiresInfo(), listReminder);

        verify(expiresDao).selectValidLibrary();
    }

    @Test
    //@Ignore
    public void test_getReminderExpiresInfo_selectValidLibrary_is_not_empty() throws IOException {

        ExpiresDao expiresDao = mock(ExpiresDao.class);

        expiresService.setExpiresDao(expiresDao);

        List<ReminderInfo> listReminder = Lists.newArrayList();

        List<Library> listLibrary = Lists.newArrayList();

        Library library = new Library();

        library.setLibId(5);
        library.setLibName("飞天技术图书馆");
        library.setLibDept("技术部");

        listLibrary.add(library);

        ReminderInfo reminderInfo = new ReminderInfo();

        listReminder.add(reminderInfo);

        when(expiresDao.getReminderExpiresInfo(library.getLibId())).thenReturn(listReminder);

        when(expiresDao.selectValidLibrary()).thenReturn(listLibrary);

        Assert.assertTrue(expiresService.getReminderExpiresInfo().size() > 0);

        verify(expiresDao).selectValidLibrary();
        verify(expiresDao).getReminderExpiresInfo(library.getLibId());

    }

}

