package com.qunar.fresh.librarysystem.dao;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-16
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class LogDaoTest {
    @Resource
    LogDao logDao;
    private LogInfo logInfo;
    private Date endTime;
    private RowBounds rowBounds;

    @Before
    public void doSetup(){
        logInfo = new LogInfo(OperationType.ADDLIBRARY,new Date(2014,3,9),1);
        logInfo.setOperatorRtx("he.chen");
        logInfo.setInfo("增加图书馆");
        endTime = new Date();
        endTime = DateUtils.addDays(endTime,1);
        rowBounds = new RowBounds(0,10);
    }
    @Test
    public void test_fetchLogInfoList(){

        List<LogInfo> logInfoList = logDao.fetchLogInfoList(logInfo, Lists.newArrayList(OperationType.ADDLIBRARY,OperationType.DELETELIBRARY), endTime, rowBounds);
    }

    @Test
    public void test_countLogInfo(){
         logDao.countLogInfo(logInfo,endTime);
    }

    @Test
    public void test_insertLog(){
        logDao.insertLog(logInfo);
    }
}
