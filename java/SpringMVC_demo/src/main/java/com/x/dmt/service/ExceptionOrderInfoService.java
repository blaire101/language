package com.x.dmt.service;

import com.google.common.collect.Lists;
import com.x.dmt.dao.main.MainExceptionOrderInfoDAO;
import com.x.dmt.dao.monitor.ExceptionOrderInfoDAO;
import com.x.dmt.dao.monitor.ExceptionShopSummaryInfoDAO;
import com.x.dmt.dao.monitor.VipShopIdInfoDAO;
import com.x.dmt.model.ExceptionOrderInfo;
import com.x.dmt.model.ShopIdInfo;
import com.x.dmt.util.CF;
import com.x.dmt.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Date : 2016-07-05
 */

public class ExceptionOrderInfoService {

    private static ExceptionOrderInfoService instance = new ExceptionOrderInfoService();

    public static ExceptionOrderInfoService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(ExceptionOrderInfoService.class);

    private ExceptionOrderInfoService() {
    }

    @Resource
    private ExceptionShopSummaryInfoDAO exceptionShopSummaryInfoDAO; // real monitor

    @Resource
    private ExceptionOrderInfoDAO exceptionOrderInfoDAO; // real monitor

    @Resource
    private VipShopIdInfoDAO vipShopIdInfoDAO; // real monitor

    @Resource
    private MainExceptionOrderInfoDAO mainExceptionOrderInfoDAO; // main rds one


    public void addALlShopExceptionOrder() {

        String statDate = CF.getStringDateYMD(new Date());

        List<ShopIdInfo> shopIdInfos = exceptionShopSummaryInfoDAO.getExceptionShopIdInfos(statDate, Constant.CHECK_TYPE_ALL, Constant.STATUS_TYPE_ERROR); // vipShopInfoDAO.getVipShopInfo();

        if (!CF.notEmptyContainer(shopIdInfos)) {
            return;
        }
        logger.info("addALlShopExceptionOrder shopIdInfos size : {}", shopIdInfos.size());

        this.addExceptionOrderInfoListToMonitor(shopIdInfos, Constant.CHECK_TYPE_ALL);
    }

    public void addVipShopExceptionOrder() {

        List<ShopIdInfo> shopIdInfos = vipShopIdInfoDAO.getVipShopIdInfos();
        logger.info("addVipShopExceptionOrder shopIdInfos size : {}", shopIdInfos.size());
        this.addExceptionOrderInfoListToMonitor(shopIdInfos, Constant.CHECK_TYPE_VIP);
    }

    private void addExceptionOrderInfoListToMonitor(List<ShopIdInfo> shopIdInfos, Integer checkType) {

        logger.info("check shop Type : {} , addExceptionOrderInfoListToMonitor function start run ...", checkType);

        String endTime = CF.getStringDateYMDHMS(CF.getEndTime(CF.getStartTime(new Date())));
        String startTime = CF.getStringDateYMDHMS(CF.getZeroToday());

        logger.info("check shop Type : {} , startTime : {} , endTime : {} .", startTime, endTime, checkType);

        Long timeDiff = Constant.TIME_DIFF; // 100000L

        for (ShopIdInfo shopIdInfo : shopIdInfos) {

            Long shopId = shopIdInfo.getShopId();

            logger.info("check shop Type : {} , shop_id : {}", checkType, shopId);

            List<ExceptionOrderInfo> delayExceptionOrderInfoList = mainExceptionOrderInfoDAO.getDelayExceptionOrderInfoList(shopId, startTime, endTime, timeDiff);
            List<ExceptionOrderInfo> syncTimeExceptionOrderInfoList = mainExceptionOrderInfoDAO.getSyncTimeExceptionOrderInfoList(shopId, startTime, endTime);
            List<ExceptionOrderInfo> paidExceptionOrderInfoList = mainExceptionOrderInfoDAO.getPaidExceptionOrderInfoList(shopId, startTime, endTime);
            List<ExceptionOrderInfo> transactionExceptionOrderInfoList = mainExceptionOrderInfoDAO.getTransactionExceptionOrderInfoList(shopId, startTime, endTime);
            List<ExceptionOrderInfo> itemAmountExceptionOrderInfoList = mainExceptionOrderInfoDAO.getItemAmountExceptionOrderInfoList(shopId, startTime, endTime);

            this.addExceptionOrderInfoList(delayExceptionOrderInfoList, shopId, startTime, endTime, checkType);
            this.addExceptionOrderInfoList(syncTimeExceptionOrderInfoList, shopId, startTime, endTime, checkType);
            this.addExceptionOrderInfoList(paidExceptionOrderInfoList, shopId, startTime, endTime, checkType);
            this.addExceptionOrderInfoList(transactionExceptionOrderInfoList, shopId, startTime, endTime, checkType);
            this.addExceptionOrderInfoList(itemAmountExceptionOrderInfoList, shopId, startTime, endTime, checkType);
        }

        logger.info("check shop Type : {} , addExceptionOrderInfoListToMonitor function run end !!!", checkType);
    }

    private void addExceptionOrderInfoList(List<ExceptionOrderInfo> exceptionOrderInfoList, Long shopId, String startTime, String endTime, Integer checkType) {
        if (!CF.notEmptyContainer(exceptionOrderInfoList)) {
            return;
        }

        List<ExceptionOrderInfo> exceptionOrderInfoListInDataMonitor = exceptionOrderInfoDAO.getExceptionOrderInfoListByShopTimeCheck(shopId, startTime, endTime, checkType);

        List<ExceptionOrderInfo> papreInsertExceptionOrderInfoList = Lists.newArrayList();

        for (ExceptionOrderInfo exceptionOrderInfo : exceptionOrderInfoList) {

            boolean insertFlag = true;
            for (ExceptionOrderInfo exceptionOrderInfoInDataMonitor : exceptionOrderInfoListInDataMonitor) {
                if(sameOrderExceptType(exceptionOrderInfo, exceptionOrderInfoInDataMonitor)) {
                    insertFlag = false;
                    break;
                }
            }
            if (insertFlag) {
                exceptionOrderInfo.setAlertType(Constant.ALERT_TYPE_NO);
                exceptionOrderInfo.setStatusType(Constant.STATUS_TYPE_ERROR);
                exceptionOrderInfo.setCheckType(checkType);
                papreInsertExceptionOrderInfoList.add(exceptionOrderInfo);
            }
        }

        if (!CF.notEmptyContainer(papreInsertExceptionOrderInfoList)) {
            return;
        }
        /** order's trade_time or sync_time maybe modify, so replace into, not insert into **/
        logger.info("check shop Type : {} , papreInsertExceptionOrderInfoList : {} ", checkType, papreInsertExceptionOrderInfoList.size());
        exceptionOrderInfoDAO.addExceptionOrderInfoList(papreInsertExceptionOrderInfoList);
    }

    private boolean sameOrderExceptType(ExceptionOrderInfo exceptionOrderInfo, ExceptionOrderInfo exceptionOrderInfoInDataMonitor) {
        if (exceptionOrderInfo.getExceptOrderNumber().equals(exceptionOrderInfoInDataMonitor.getExceptOrderNumber())
                && exceptionOrderInfo.getExceptType().equals(exceptionOrderInfoInDataMonitor.getExceptType())) {
            return true;
        }
        return false;
    }
}
