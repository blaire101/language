package com.x.dmt.util;

import com.google.common.base.Preconditions;
import com.x.dmt.model.ExceptionOrderInfo;
import com.x.dmt.model.ShopIdInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Date : 2016-07-05
 */
public class CF {

    private static Logger logger = LoggerFactory.getLogger(CF.class);

    public static void showList(List<ShopIdInfo> shopIdInfoList) {

        for (ShopIdInfo shopIdInfo : shopIdInfoList) {
            System.out.println(shopIdInfo);
        }
    }

    public static void showExceptionOrderInfoList(String msg, List<ExceptionOrderInfo> exceptionOrderInfoList) {
        if (exceptionOrderInfoList != null && !exceptionOrderInfoList.isEmpty()) {
            logger.info(msg);
        }

        for (ExceptionOrderInfo exceptionOrderInfo : exceptionOrderInfoList) {
            logger.info(exceptionOrderInfo.toString());
        }
    }

    public static boolean notEmptyContainer(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Date getZeroToday() {
        Date curDate = new Date();
        String curTimeString = new SimpleDateFormat(Constant.DATE_FOMMAT_YMD).format(curDate);
        Date zoneDate = null;
        try {
            zoneDate = new SimpleDateFormat(Constant.DATE_FOMMAT_YMD).parse(curTimeString);
        } catch (ParseException e) {
            logger.error("getZeroTime ParseException {}", e.getMessage());
        }
        Preconditions.checkArgument(zoneDate != null);
        return zoneDate;
    }

    public static Date getCurHourStartTime(Date curDate) {
        String startTimeString = new SimpleDateFormat(Constant.DATE_FOMMAT_YMDHMS).format(new Date());

        Date startTime = null;

        try {
            startTime = new SimpleDateFormat(Constant.DATE_FOMMAT_YMDH).parse(startTimeString);
        } catch (ParseException e) {
            logger.error("getStartTime ParseException {}", e.getMessage());
        }
        Preconditions.checkArgument(startTime != null);

        Date zeroDate = getZeroToday();
        if (startTime.getTime() < zeroDate.getTime()) {
            startTime = zeroDate;
        }

        return startTime;
    }

    public static Date getStartTime(Date curDate) {
        Date tmpTime = new Date(curDate.getTime() - Constant.HOUR_MS);
        String startTimeString = new SimpleDateFormat(Constant.DATE_FOMMAT_YMDHMS).format(tmpTime);

        Date startTime = null;

        try {
            startTime = new SimpleDateFormat(Constant.DATE_FOMMAT_YMDH).parse(startTimeString);
        } catch (ParseException e) {
            logger.error("getStartTime ParseException {}", e.getMessage());
        }
        Preconditions.checkArgument(startTime != null);

        Date zeroDate = getZeroToday();
        if (startTime.getTime() < zeroDate.getTime()) {
            startTime = zeroDate;
        }

        return startTime;
    }

    public static Date getEndTime(Date curDate) {
        Date endTime = new Date(curDate.getTime() + Constant.HOUR_MS);
        return endTime;
    }

    public static Date getCurMonthFirstDayTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2016);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();//当前月最后一天

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDate = cal.getTime();//当前月第一天

//        System.out.println(firstDate);
        return firstDate;
    }
    public static String getStringDateYMDHMS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_YMDHMS);
        return sdf.format(date);
    }

    public static String getStringDateYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_YMD);
        return sdf.format(date);
    }

    public static String getStrYMDDateByTodayAddDays(int cnt) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_YMD);
        Date yesterday = DateUtils.addDays(new Date(), cnt);
        String strDate = sdf.format(yesterday);
        return strDate;
    }

    public static void main(String[] args) {

        Long L1 = new Long(1);
        Long L2 = new Long(1);
        System.out.println(L1 == L2);
        System.out.println(L1.equals(L2));
        Integer I1 = new Integer(1);
        Integer I2 = new Integer(1);

        System.out.println(I1 == I2);
        System.out.println(I1.equals(I2));

        System.out.println("----");
        Date D1 = new Date();
        Date D2 = new Date();

        System.out.println(D1);
        System.out.println(D2);

        System.out.println(D1.equals(D2));

        System.out.println(D1.getTime());

        System.out.println(CF.getStringDateYMD(new Date()));
    }

}
