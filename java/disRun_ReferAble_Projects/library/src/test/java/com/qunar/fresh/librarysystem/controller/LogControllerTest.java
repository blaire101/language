package com.qunar.fresh.librarysystem.controller;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.PageTurningInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-8
 * Time: 下午8:30
 * To change this template use File | Settings | File Templates.
 */
public class LogControllerTest {
    private LogService logService;
    private LogController logController;
    @Before
    public void doSetup(){
        logService = mock(LogService.class);
        logController = new LogController();
    }

    @Test
    public void testFetchLogInfo(){
        Date startTime = new Date();
        Date endTime = new Date();
        Date nextEndTime = DateUtils.addDays(endTime, 1);
        LogInfo logInfo = mock(LogInfo.class);
        when(logService.getLogInfo(OperationType.ADDLIBRARY,startTime,1)).thenReturn(logInfo);
        when(logService.fetchLogInfoList(Lists.newArrayList(logInfo),nextEndTime ,1,10)).thenReturn(new ArrayList<Map<String, Object>>());
        when(logService.getPageTurningInof(Lists.newArrayList(logInfo),nextEndTime,1,10)).thenReturn(new PageTurningInfo());
        verify(logService).fetchLogInfoList(Lists.newArrayList(logInfo),nextEndTime,1,10);
        verify(logService).getPageTurningInof(Lists.newArrayList(logInfo),nextEndTime,1,10);
    }

    @Test
    public void testFetchLogInfo_should_return_errcode0_when_logList_is_notNull(){
        Date startTime = new Date();
        Date endTime = new Date();
        Date nextEndTime = DateUtils.addDays(endTime, 1);
        LogInfo logInfo = mock(LogInfo.class);
        when(logService.getLogInfo(OperationType.ADDLIBRARY,startTime,1)).thenReturn(logInfo);
        when(logService.fetchLogInfoList(Lists.newArrayList(logInfo),nextEndTime ,1,10)).thenReturn(new ArrayList<Map<String,Object>>());
        when(logService.getPageTurningInof(Lists.newArrayList(logInfo),nextEndTime,1,10)).thenReturn(new PageTurningInfo());
    }

    @Test
    public void testFetchLogInfo_should_return_errcode1_when_logList_is_null(){
        Date startTime = new Date();
        Date endTime = new Date();
        Date nextEndTime = DateUtils.addDays(endTime, 1);
        LogInfo logInfo = mock(LogInfo.class);
        when(logService.getLogInfo(OperationType.ADDLIBRARY,startTime,1)).thenReturn(logInfo);
        when(logService.fetchLogInfoList(Lists.newArrayList(logInfo),nextEndTime ,1,10)).thenReturn(null);
        when(logService.getPageTurningInof(Lists.newArrayList(logInfo),nextEndTime,1,10)).thenReturn(new PageTurningInfo());
    }
}
