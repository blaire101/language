package com.qunar.fresh.librarysystem.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.qunar.fresh.librarysystem.dao.SystemServiceDao;
import com.qunar.fresh.librarysystem.model.SystemParam;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
* Created with IntelliJ IDEA.
* User: yingnan.zhang
* Date: 14-4-9
* Time: 下午12:51
* To change this template use File | Settings | File Templates.
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class SystemServiceTest {
    @Resource
    private SystemService systemService;

    private SystemServiceDao systemServiceDao;
    private SystemParam systemParam, systemParamD;
    private LogService logService;
    @Before
    public void beforeParameterParse() {
        systemParam = new SystemParam();
        systemParamD = new SystemParam();
        systemParam.setBorrowPeriod(10);
        systemParam.setBorrowTotalNum(5);
        systemParam.setRedecorateNum(5);
        systemParam.setRedecoratePeriod(10);
        systemParam.setRemindDay(12);
        systemParamD.setBorrowPeriod(10);
        systemParamD.setBorrowTotalNum(2);
        systemParamD.setRedecorateNum(6);
        systemParamD.setRedecoratePeriod(10);
        systemParamD.setRemindDay(12);
        systemServiceDao = mock(SystemServiceDao.class);
    }

   @Test
   @Ignore
    public void test_ParameterParseNull() {
        Map<String,Object> result = null;
        when(systemServiceDao.fetchSystemCount(0)).thenReturn(1);
        when(systemServiceDao.fetchSystemData(0)).thenReturn(systemParam);
        systemService.setSystemParamDao(systemServiceDao);
        result = systemService.parameterParse("10", "-5", "-6", "10", "12", 0, "yaya");
        Assert.assertTrue(result.get("ret").equals(1) == false);
        Assert.assertTrue(((Integer)result.get("browTotalNum")).equals(5));
        Assert.assertTrue(((Integer)result.get("redecPeriod")).equals(10));
        verify(systemServiceDao);
    }

    @Test
    @Ignore
    public void test_ParameterParseNotNull() {
        Map<String,Object> result = null;
        String differString;
        Date date = new Date();
        Timestamp NowDate = new Timestamp(date.getTime());
        differString = systemService.differContext(systemParam, systemParamD);
        when(systemServiceDao.fetchSystemCount(1)).thenReturn(1);
        when(systemServiceDao.fetchSystemData(1)).thenReturn(systemParam);
        when(systemServiceDao.updateSystemData(1, 2, 10, 10, 6, 12)).thenReturn(1);
        systemService.setSystemParamDao(systemServiceDao);
        result = systemService.parameterParse("10", "2", "10", "6", "12", 1, "yaya");
        verify(systemServiceDao);
        Assert.assertTrue(((Integer)result.get("browTotalNum")).equals(2));
        Assert.assertTrue(((Integer)result.get("browPeriod")).equals(10));
        Assert.assertTrue(((Integer)result.get("redecPeriod")).equals(10));
        Assert.assertTrue(((Integer)result.get("redecCount")).equals(6));
        Assert.assertTrue(((Integer)result.get("remindTime")).equals(12));
        Assert.assertTrue((Boolean)result.get("ret") == true);
    }

    @Test
    @Ignore
    public void test_fetchAdminls() {
        when(systemServiceDao.fetchAdminls("yaya")).thenReturn(null);
        systemService.setSystemParamDao(systemServiceDao);
        Assert.assertNull(systemService.fetchAdminls("yaya"));
        verify(systemServiceDao);
    }

    @Test
    @Ignore
    public void test_getCheckOut() {
        Assert.assertTrue(systemService.getCheckOut("", "", "", "", "") == false);
        Assert.assertTrue(systemService.getCheckOut(null, null, null, null, null) == false);
        Assert.assertTrue(systemService.getCheckOut("", "12", "", "", "") == true);
    }

    @Test
    @Ignore
    public void test_getBorrowPeriod() {
        Integer libId = 12;
        when(systemServiceDao.getBorrowPeriod(12)).thenReturn(null);
        systemService.setSystemParamDao(systemServiceDao);
        Assert.assertTrue(systemService.getBorrowPeriod(libId) == -1);
    }
}
