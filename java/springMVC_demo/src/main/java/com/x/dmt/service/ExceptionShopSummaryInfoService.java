package com.x.dmt.service;

import com.google.common.collect.Lists;
import com.x.dmt.dao.monitor.ExceptionShopSummaryInfoDAO;
import com.x.dmt.dao.statp2.StatExceptionShopSummaryInfoDAO;
import com.x.dmt.model.ExceptionShopSummaryInfo;
import com.x.dmt.util.CF;
import com.x.dmt.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Date : 2016-07-20
 */

public class ExceptionShopSummaryInfoService {

    private static ExceptionShopSummaryInfoService instance = new ExceptionShopSummaryInfoService();

    public static ExceptionShopSummaryInfoService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(ExceptionShopSummaryInfoService.class);

    private ExceptionShopSummaryInfoService() {
    }

    @Resource
    private StatExceptionShopSummaryInfoDAO statExceptionShopSummaryInfoDAO; // stat db

    @Resource
    private ExceptionShopSummaryInfoDAO monitorExceptionShopSummaryInfoDAO; // real_monitor

    public void addExceptionShopSummaryInfo() {

        logger.info("ExceptionShopSummaryInfoService addExceptionShopSummaryInfo() start...");

        String statDate = CF.getStringDateYMD(new Date());

        Integer checkType = Constant.CHECK_TYPE_ALL; /** ALL SHOP, CHECK_TYPE = 2 **/

        List<ExceptionShopSummaryInfo> statExceptionShopSummaryInfoList = this.getExceptionShopSummaryInfoListFromStatP2BystatDate(statDate);

        List<ExceptionShopSummaryInfo> dataMonitorExceptionShopSummaryInfoList = this.getExceptionShopSummaryInfoListByStatDateCheckAll(statDate, checkType);

        this.addExceptionShopSummaryInfoList(statExceptionShopSummaryInfoList, dataMonitorExceptionShopSummaryInfoList, checkType);

        logger.info("ExceptionShopSummaryInfoService addExceptionShopSummaryInfo() end !!!");
    }

    /**
     * 根据 statDate 得到 异常的商户汇总信息, (get today Exception shop summary)
     * @param statDate
     * @return
     */
    private List<ExceptionShopSummaryInfo> getExceptionShopSummaryInfoListFromStatP2BystatDate(String statDate) {

        logger.info("getExceptionShopSummaryInfoListFromStatP2BystatDate() start...");

        logger.info("statDate : {}", statDate);

        List<ExceptionShopSummaryInfo> exceptionShopSummaryInfoList = statExceptionShopSummaryInfoDAO.getExceptionShopSummaryInfo(statDate);

        logger.info("getExceptionShopSummaryInfoListFromStatP2BystatDate() end !!!");

        return exceptionShopSummaryInfoList;
    }

    private List<ExceptionShopSummaryInfo> getExceptionShopSummaryInfoListByStatDateCheckAll(String stringStatDate, Integer checkType) {

        logger.info("getExceptionShopSummaryInfoListByStatDateCheckAll() start...");

        logger.info("statDate : {}", stringStatDate);

        /**
           get today ALL exception—shop-summary
         */
        List<ExceptionShopSummaryInfo> exceptionShopSummaryInfoList = monitorExceptionShopSummaryInfoDAO.getExceptionShopSummaryInfoListByStatDateCheckType(stringStatDate, checkType);

        logger.info("getExceptionShopSummaryInfoListByStatDateCheckAll() end !!!");

        return exceptionShopSummaryInfoList;
    }

    private void addExceptionShopSummaryInfoList(List<ExceptionShopSummaryInfo> statExceptionShopSummaryInfoList, List<ExceptionShopSummaryInfo> dataMonitorExceptionShopSummaryInfoList, Integer checkType) {
        if (!CF.notEmptyContainer(statExceptionShopSummaryInfoList)) {
            return;
        }

        List<ExceptionShopSummaryInfo> prepareInsertExceptionShopSummaryInfoList = Lists.newArrayList();

        for (ExceptionShopSummaryInfo statExceptionShopSummaryInfo : statExceptionShopSummaryInfoList) {

            boolean insertFlag = true;
            for (ExceptionShopSummaryInfo dataMonitorExceptionShopSummaryInfo : dataMonitorExceptionShopSummaryInfoList) {
                if(sameShopSummaryInfo(statExceptionShopSummaryInfo, dataMonitorExceptionShopSummaryInfo)) {
                    if (dataMonitorExceptionShopSummaryInfo.getStatusType().equals(Constant.STATUS_TYPE_WARNING)) { // need update to error
                        this.monitorExceptionShopSummaryInfoDAO.updateExceptionShopSummaryInfoListStatusType(Constant.STATUS_TYPE_ERROR, dataMonitorExceptionShopSummaryInfo.getId());
                        insertFlag = false;
                        break;
                    } else if (dataMonitorExceptionShopSummaryInfo.getStatusType().equals(Constant.STATUS_TYPE_ERROR)) {
                        insertFlag = false;
                        break;
                    }
                }
            }

            if (insertFlag) { // need insert to db
                statExceptionShopSummaryInfo.setAlertType(Constant.ALERT_TYPE_NO); // 0
                statExceptionShopSummaryInfo.setStatusType(Constant.STATUS_TYPE_WARNING); // 1
                statExceptionShopSummaryInfo.setCheckType(checkType); // 2
                prepareInsertExceptionShopSummaryInfoList.add(statExceptionShopSummaryInfo);
            }
        }

        if (!CF.notEmptyContainer(prepareInsertExceptionShopSummaryInfoList)) {
            return;
        }
        logger.info("prepareReplaceExceptionShopSummaryInfoList : {}", prepareInsertExceptionShopSummaryInfoList.size());
        monitorExceptionShopSummaryInfoDAO.addExceptionShopSummaryInfoList(prepareInsertExceptionShopSummaryInfoList);
    }

    private boolean sameShopSummaryInfo(ExceptionShopSummaryInfo statExceptionShopSummaryInfo, ExceptionShopSummaryInfo dataMonitorExceptionShopSummaryInfo) {
        if (CF.getStringDateYMD(statExceptionShopSummaryInfo.getStatDate()).equals(CF.getStringDateYMD(dataMonitorExceptionShopSummaryInfo.getStatDate()))
                && statExceptionShopSummaryInfo.getShopId().equals(dataMonitorExceptionShopSummaryInfo.getShopId())
                && statExceptionShopSummaryInfo.getExceptType().equals(dataMonitorExceptionShopSummaryInfo.getExceptType())
                ) {
            return true;
        }
        return false;
    }
}
