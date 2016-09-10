package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.LogDao;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-7
 * Time: 下午10:39
 * To change this template use File | Settings | File Templates.
 */
public class LogServiceTest {
    private LogService logService;
    private LogDao logDao;

    @Before
    public void doSetup(){
        logDao = mock(LogDao.class);
        logService = new LogService();
        logService.setLogDao(logDao);
    }

    @Test
    public void testInsertLog(){
        LogInfo logInfo = new LogInfo();
        logService.insertLog(logInfo);
        verify(logDao).insertLog(logInfo);
    }
    @Test
    @Ignore
    public void testFetchLogInfoList(){
        LogInfo logInfo = new LogInfo();
        Date endTime = new Date();
        RowBounds rowBounds = new RowBounds(0,10);
        RowBounds rowBounds1 = rowBounds;
        OperationType currOper = logInfo.getOperationType();
        OperationType preOper = OperationType.values()[currOper.code() - 1];
        List<OperationType> operationTypeList = Lists.newArrayList(currOper, preOper);
        when(logDao.fetchLogInfoList(logInfo,operationTypeList,endTime,rowBounds)).thenReturn(new ArrayList<LogInfo>());
        logService.fetchLogInfoList(Lists.newArrayList(logInfo),endTime,1,10);
        verify(logDao).fetchLogInfoList(logInfo,operationTypeList,endTime,rowBounds);
    }

    @Test
    public void testCountLogInfo(){
        LogInfo logInfo = new LogInfo();
        Date endTime = new Date();
        when(logDao.countLogInfo(logInfo,endTime)).thenReturn(new Integer(2));
        logService.countLogInfo(logInfo,endTime);
        verify(logDao).countLogInfo(logInfo,endTime);
    }
}
