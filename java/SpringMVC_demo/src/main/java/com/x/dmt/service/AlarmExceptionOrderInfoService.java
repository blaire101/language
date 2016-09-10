package com.x.dmt.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.x.dmt.dao.monitor.ExceptionOrderInfoDAO;
import com.x.dmt.dao.monitor.ExceptionShopSummaryInfoDAO;
import com.x.dmt.dao.monitor.VipShopIdInfoDAO;
import com.x.dmt.email.LoadMailConfig;
import com.x.dmt.email.MailManager;
import com.x.dmt.model.ExceptionOrderInfo;
import com.x.dmt.model.ShopIdInfo;
import com.x.dmt.util.CF;
import com.x.dmt.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016-07-21
 */

public class AlarmExceptionOrderInfoService {

    private static AlarmExceptionOrderInfoService instance = new AlarmExceptionOrderInfoService();

    public static AlarmExceptionOrderInfoService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(AlarmExceptionOrderInfoService.class);

    private AlarmExceptionOrderInfoService() {}

    @Resource
    private VipShopIdInfoDAO vipShopIdInfoDAO; // real monitor

    @Resource
    private ExceptionShopSummaryInfoDAO exceptionShopSummaryInfoDAO; // real monitor

    @Resource
    private ExceptionOrderInfoDAO exceptionOrderInfoDAO; // real monitor

    @Resource
    private MailManager mailManager;

    public void alertALlShopExceptionOrder() {

        String statDateString = CF.getStringDateYMD(new Date());

        List<ShopIdInfo> shopIdInfos = exceptionShopSummaryInfoDAO.getExceptionShopIdInfos(statDateString, Constant.CHECK_TYPE_ALL, Constant.STATUS_TYPE_ERROR);

        Date startTimeDate = CF.getCurHourStartTime(new Date());
        String startTime = CF.getStringDateYMDHMS(startTimeDate); // now, 9:20, then startTime 9:00
        String endTime = CF.getStringDateYMDHMS(CF.getEndTime(startTimeDate)); // now, 9:20, then endTime 10:00

        logger.info("alertALlShopExceptionOrder() startTime : {}, endTime : {}", startTime, endTime);

        Integer alertType = Constant.ALERT_TYPE_NO;
        Integer statusType = Constant.STATUS_TYPE_ERROR;
        Integer checkType = Constant.CHECK_TYPE_ALL;

        Map<Long, List<ExceptionOrderInfo>> shopIdExceptOrderMap = this.getExceptionOrderInfoList(shopIdInfos, startTime, endTime, alertType, statusType, checkType);

        String mailCoreCentent = this.getMailCoreContent(shopIdExceptOrderMap);

        sendMailAlert(checkType, startTime, mailCoreCentent);
    }

    public void alertVipShopExceptionOrder() {

        List<ShopIdInfo> shopIdInfos = vipShopIdInfoDAO.getVipShopIdInfos();

        Date startTimeDate = CF.getCurHourStartTime(new Date());
        String startTime = CF.getStringDateYMDHMS(startTimeDate); // now, 9:20, then startTime 9:00
        String endTime = CF.getStringDateYMDHMS(CF.getEndTime(startTimeDate)); // now, 9:20, then endTime 10:00

        logger.info("alertVipShopExceptionOrder() startTime : {}, endTime : {}", startTime, endTime);
        Integer alertType = Constant.ALERT_TYPE_NO;
        Integer statusType = Constant.STATUS_TYPE_ERROR;
        Integer checkType = Constant.CHECK_TYPE_VIP;

        Map<Long, List<ExceptionOrderInfo>> shopIdExceptOrderMap = this.getExceptionOrderInfoList(shopIdInfos, startTime, endTime, alertType, statusType, checkType);

        String mailCoreCentent = this.getMailCoreContent(shopIdExceptOrderMap);

        sendMailAlert(checkType, startTime, mailCoreCentent);
    }

    /** get alert_type = NO, status_type = ERROR, check_type = someOne **/
    private Map<Long, List<ExceptionOrderInfo>> getExceptionOrderInfoList(List<ShopIdInfo> shopIdInfos, String startTime, String endTime, Integer alertType, Integer statusType, Integer checkType) {

        logger.info("getExceptionOrderInfoList by shopIdInfos, String startTime, String endTime, Integer alertType, Integer statusType, Integer checkType  start run ...");

        logger.info("startTime : {} , endTime : {} .", startTime, endTime);

        Map<Long, List<ExceptionOrderInfo>> shopIdExceptOrderMap = Maps.newHashMap();

        for (ShopIdInfo shopIdInfo : shopIdInfos) {
            List<ExceptionOrderInfo> exceptionOrderInfoList = Lists.newArrayList();
            Long shopId = shopIdInfo.getShopId();

            exceptionOrderInfoList = this.exceptionOrderInfoDAO.getExceptionOrderInfoListByShopTimeAlertStatusCheck(shopId, startTime, endTime, alertType, statusType, checkType);

            if (!exceptionOrderInfoList.isEmpty()) {
                shopIdExceptOrderMap.put(shopId, exceptionOrderInfoList);
            }

            this.exceptionOrderInfoDAO.updateExceptionOrderInfoListAlertTypeByAlertCheck(Constant.ALERT_TYPE_YES, Constant.ALERT_TYPE_NO, shopId, startTime, endTime, checkType);
        }

        logger.info("getExceptionOrderInfoList by shopIdInfos, String startTime, String endTime, Integer alertType, Integer statusType, Integer checkType   run end !!!");

        return shopIdExceptOrderMap;
    }

    private synchronized void sendMailAlert(Integer checkType, String startTime, String mailCoreCentent) {
        if (StringUtils.isEmpty(mailCoreCentent)) {
            return;
        }
        String receivers = LoadMailConfig.getInstance().getMailConfigBykey("toAddressDefault");

        String mySubject = Constant.MAIL_DEFAULT_SUBJECT;

        if (checkType.equals(Constant.CHECK_TYPE_VIP)) {
            mySubject =  Constant.VIP_SHOP_MAIL_SUBJECT + startTime;
            receivers = LoadMailConfig.getInstance().getMailConfigBykey("ToAddressVipShop");

        } else if (checkType.equals(Constant.CHECK_TYPE_ALL)) {
            mySubject =  Constant.ALL_SHOP_MAIL_SUBJECT + startTime;
            receivers = LoadMailConfig.getInstance().getMailConfigBykey("ToAddressALLShop");
        }
        try {
            mailManager.sendMail(receivers, mySubject, mailCoreCentent);
        } catch (UnsupportedEncodingException e) {
            logger.error("sendMail() exception : {}", e.getMessage());
        }
        return;
    }

    private String getMailCoreContent(Map<Long, List<ExceptionOrderInfo>> shopIdExceptOrderMap) {

        if (null == shopIdExceptOrderMap || shopIdExceptOrderMap.isEmpty()) {
            return null;
        }

        StringBuilder mailCoreContent = new StringBuilder("");

        mailCoreContent.append(Constant.EXCEPT_SHOP_COUNT_DESC).append(shopIdExceptOrderMap.size()).append("\n");
        mailCoreContent.append(Constant.EXCEPT_SHOP_ID_DESC);

        boolean first = true;
        for (Long s : shopIdExceptOrderMap.keySet()) {
            if (first) {
                mailCoreContent.append(s);
                first = false;
            } else {
                mailCoreContent.append(", " + s);
            }
        }
        mailCoreContent.append("\n");
        /** online check time**/
        String startTime = CF.getStringDateYMDHMS(CF.getStartTime(new Date()));
        String endTime = CF.getStringDateYMDHMS(CF.getEndTime(CF.getStartTime(new Date())));
        mailCoreContent.append(Constant.CHECK_TIME_RANGE_DESC).append(startTime).append(" -> ").append(endTime).append("\n");
        mailCoreContent.append(Constant.SPLIT_LINE).append("\n详情如下 : \n\n");

        for (Map.Entry<Long, List<ExceptionOrderInfo>> entry : shopIdExceptOrderMap.entrySet()) {
            Long shopId = entry.getKey();

            mailCoreContent.append(Constant.SHOP_ID_DESC).append(shopId).append(" , " + Constant.SHOP_NAME_DESC);

            for (ExceptionOrderInfo exceptionOrderInfo : entry.getValue()) {
                mailCoreContent.append(exceptionOrderInfo.getShopName()).append("\n");
                break;
            }

            for (ExceptionOrderInfo exceptionOrderInfo : entry.getValue()) {
                mailCoreContent
                        .append(Constant.ORDER_NUMBER_DESC).append(exceptionOrderInfo.getExceptOrderNumber()).append(" , ")
                        .append(Constant.EXCEPT_SHOW_DESC).append(getExceptShow(exceptionOrderInfo.getExceptType())).append("  ")
                        .append("\n");
            }
            mailCoreContent.append(Constant.SPLIT_LINE + "\n");
        }
        logger.info("mailCoreContent : \n" + mailCoreContent);

        return mailCoreContent.toString();
    }

    private String getExceptShow(Integer exceptType) {
        String exceptShow = new String();
        switch (exceptType.intValue()) {
            case 1:
                exceptShow = Constant.EXCEPT_TYPE_SHOW_1;
                break;
            case 2:
                exceptShow = Constant.EXCEPT_TYPE_SHOW_2;
                break;
            case 3:
                exceptShow = Constant.EXCEPT_TYPE_SHOW_3;
                break;
            case 4:
                exceptShow = Constant.EXCEPT_TYPE_SHOW_4;
                break;
            case 5:
                exceptShow = Constant.EXCEPT_TYPE_SHOW_5;
                break;
        }
        if (StringUtils.isEmpty(exceptShow)) {
            logger.error("getExceptShow exceptType is error !");
        }
        return exceptShow;
    }
}
