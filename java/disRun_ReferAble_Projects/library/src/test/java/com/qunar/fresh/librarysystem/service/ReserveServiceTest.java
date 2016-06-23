package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.ReserveDao;
import com.qunar.fresh.librarysystem.email.TaskManager;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.junit.Assert;
import org.junit.Ignore;
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
 * Date: 14-4-06
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class ReserveServiceTest {

    @Resource
    private ReserveService reserveService;

    private final static int reserveFlag = 0;

    public final static int reserveSuccess = 1;

    public final static int existInLib = 2;

    public final static int isReserved = 3;

    public final static int unknownError = 4;

    public final static int parameterError = 5;

    @Test
    @Ignore
    public void test_dealReserveInfo_isConExistBookInfoIdAndLibId() throws IOException {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);
        int bookInfoId = 4;
        int bookLib = 5;
        String userRtx = "libin.chen";
        when(reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib)).thenReturn(0);

        org.junit.Assert.assertEquals(reserveService.dealReserveInfo(bookInfoId, userRtx, bookLib), parameterError);

        verify(reserveDao).isConExistBookInfoIdAndLibId(bookInfoId, bookLib);
    }
    @Test
    @Ignore
    public void test_dealReserveInfo_isExistInLib() throws IOException {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);
        int bookInfoId = 4;
        int bookLib = 5;
        String userRtx = "libin.chen";
        when(reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib)).thenReturn(1);

        when(reserveDao.isStillExistInLib(bookInfoId, bookLib).intValue()).thenReturn(1);

        org.junit.Assert.assertEquals(reserveService.dealReserveInfo(bookInfoId, userRtx, bookLib), existInLib);

        verify(reserveDao).isConExistBookInfoIdAndLibId(bookInfoId, bookLib);

        verify(reserveDao).isStillExistInLib(bookInfoId, bookLib);
    }

    @Test
    @Ignore
    public void test_dealReserveInfo_isNotExistInLib_isReserved() throws IOException {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);
        int bookInfoId = 1;
        int bookLib = 5;
        String userRtx = "libin.chen";
        when(reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib)).thenReturn(1);
        when(reserveDao.isStillExistInLib(bookInfoId, bookLib).intValue()).thenReturn(0);
        when(reserveDao.isReserved(userRtx, bookInfoId, bookLib).intValue()).thenReturn(1);

        org.junit.Assert.assertEquals(reserveService.dealReserveInfo(bookInfoId, userRtx, bookLib), isReserved);

        verify(reserveDao).isConExistBookInfoIdAndLibId(bookInfoId, bookLib);
        verify(reserveDao).isStillExistInLib(bookInfoId, bookLib);
        verify(reserveDao).isReserved(userRtx, bookInfoId, bookLib);
    }

    @Test
    //@Ignore
    public void test_dealReserveInfo_isNotExistInLib_isNotReserved() throws IOException {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);
        int bookInfoId = 1;
        int bookLib = 5;
        String userRtx = "libin.chen";
        when(reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib)).thenReturn(1);
        when(reserveDao.isStillExistInLib(bookInfoId, bookLib).intValue()).thenReturn(0);
        when(reserveDao.isReserved(userRtx, bookInfoId, bookLib).intValue()).thenReturn(0);
        when(reserveDao.addReserveInfo(bookInfoId, userRtx, bookLib)).thenReturn(1);
        org.junit.Assert.assertEquals(reserveService.dealReserveInfo(bookInfoId, userRtx, bookLib), reserveSuccess);

        verify(reserveDao).isConExistBookInfoIdAndLibId(bookInfoId, bookLib);
        verify(reserveDao).isStillExistInLib(bookInfoId, bookLib);
        verify(reserveDao).isReserved(userRtx, bookInfoId, bookLib);
        verify(reserveDao).addReserveInfo(bookInfoId, userRtx, bookLib);
    }

    @Test
    //@Ignore
    public void test_dealReserveInfo_isNotExistInLib_isNotReserved_addReserveInfo_isFail() throws IOException {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);
        int bookInfoId = 1;
        int bookLib = 5;
        String userRtx = "libin.chen";
        when(reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib)).thenReturn(1);
        when(reserveDao.isStillExistInLib(bookInfoId, bookLib).intValue()).thenReturn(0);
        when(reserveDao.isReserved(userRtx, bookInfoId, bookLib).intValue()).thenReturn(0);
        when(reserveDao.addReserveInfo(bookInfoId, userRtx, bookLib)).thenReturn(0);

        org.junit.Assert.assertEquals(reserveService.dealReserveInfo(bookInfoId, userRtx, bookLib), unknownError);

        verify(reserveDao).isConExistBookInfoIdAndLibId(bookInfoId, bookLib);
        verify(reserveDao).isStillExistInLib(bookInfoId, bookLib);
        verify(reserveDao).isReserved(userRtx, bookInfoId, bookLib);
        verify(reserveDao).addReserveInfo(bookInfoId, userRtx, bookLib);
    }

    @Test
    public void test_selectReminderReserveInfo() {
        ReserveDao reserveDao = mock(ReserveDao.class);

        reserveService.setReserveDao(reserveDao);

        List<ReminderInfo> list = Lists.newArrayList();

        when(reserveDao.selectReminderReserveInfo()).thenReturn(list);

        reserveService.selectReminderReserveInfo();

        verify(reserveDao).selectReminderReserveInfo();
    }

    @Test
    public void test_dealReminderReserveUser_notExistRemindUser() {
        ReserveDao reserveDao = mock(ReserveDao.class);
        reserveService.setReserveDao(reserveDao);

        List<ReminderInfo> list = Lists.newArrayList();

        when(reserveDao.selectReminderReserveInfo()).thenReturn(list);

        Assert.assertEquals(reserveService.dealReminderReserveUser(), 0);

        verify(reserveDao).selectReminderReserveInfo();
    }

    @Test
    public void test_dealReminderReserveUser_ExistRemindUser() {  // 未写完
        ReserveDao reserveDao = mock(ReserveDao.class);

        reserveService.setReserveDao(reserveDao);

        List<ReminderInfo> list = Lists.newArrayList();

        ReminderInfo reminderInfo = new ReminderInfo();

        list.add(reminderInfo);

        when(reserveDao.selectReminderReserveInfo()).thenReturn(list);

        TaskManager taskManager = mock(TaskManager.class);

        Assert.assertEquals(reserveService.dealReminderReserveUser(), 1);

        verify(reserveDao).selectReminderReserveInfo();

    }

    @Test
    public void test_updateReserve() {
        ReserveDao reserveDao = mock(ReserveDao.class);

        reserveService.setReserveDao(reserveDao);

        int reserveId = 24;

        when(reserveDao.updateReserve(reserveId)).thenReturn(1);

        reserveService.updateReserve(reserveId);

        verify(reserveDao).updateReserve(reserveId);
    }
}

