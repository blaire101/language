package com.x.dmt.service;

import com.google.common.collect.Lists;
import com.x.dmt.dao.statp2.EtlShopInfoDAO;
import com.x.dmt.dao.statp2.EtlStatOrderInfoDAO;

import com.x.dmt.model.EtlShopInfo;
import com.x.dmt.model.EtlStatOrderInfo;
import com.x.dmt.util.CF;
import com.x.dmt.util.Constant;
import com.x.dmt.util.EsHandler;
import com.x.dmt.util.esjar.JsonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date : 2016-08-28
 */

public class DataImportService {

    private static DataImportService instance = new DataImportService();

    public static DataImportService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);

    private DataImportService() {
    }

    @Resource
    private EtlStatOrderInfoDAO etlStatOrderInfoDAO;

    @Resource
    private EtlShopInfoDAO etlShopInfoDAO;

    public void runImportShopInfo() {

        logger.info("runImportShopInfo start...");

        for (int i = Constant.START_ADD_DAYS; i <= Constant.RECOVERY_ADD_DAYS; i++) {
            String strDate = CF.getStrYMDDateByTodayAddDays(-1 * i);

            String strYesDate = strDate;

            List<EtlShopInfo> etlShopInfoList = etlShopInfoDAO.getEtlShopInfo(strYesDate);

            logger.info("etlShopInfoList : {}, {}", strYesDate, etlShopInfoList.size());

            String _index = ResourceBundle.getBundle("props.es_config").getString("shops_index");
            String _type = ResourceBundle.getBundle("props.es_config").getString("shops_type");

            List<String> jsonDatas = Lists.newArrayList();
            List<String> ids = Lists.newArrayList();

            for (EtlShopInfo etlShopInfo : etlShopInfoList) {
                etlShopInfo = getNewEtlShopInfo(etlShopInfo);
                jsonDatas = getJsonList(jsonDatas, etlShopInfo);
                ids.add("" + etlShopInfo.getId());
                if (jsonDatas.size() == Constant.INSERT_ES_SIZE) {
                    insertEsReplaceIndexBatch(_index, _type, jsonDatas, ids);
                    continue;
                }
            }
            logger.info("jsonDatas.size() : {}", jsonDatas.size());
            insertEsReplaceIndexBatch(_index, _type, jsonDatas, ids);
//        EsHandler.getInstance().getElasticSearchHandler().close();
        }
        logger.info("runImportShopInfo end !");

    }
    public void runImportStatOrderInfo() {
        logger.info("runImportStatOrderInfo start...");

        for (int i = Constant.START_ADD_DAYS; i <= Constant.RECOVERY_ADD_DAYS; i++) {
            String strDate = CF.getStrYMDDateByTodayAddDays(-1 * i);

            String startDate = strDate;
            String endDate = strDate;

            List<EtlStatOrderInfo> etlStatOrderInfoList = etlStatOrderInfoDAO.getEtlStatOrderInfo(startDate, endDate);

            logger.info("etlStatOrderInfoList : {} ~ {}, {}", startDate, endDate, etlStatOrderInfoList.size());

            String _index = ResourceBundle.getBundle("props.es_config").getString("stat_order_index");
            String _type = ResourceBundle.getBundle("props.es_config").getString("stat_order_type");

            List<String> jsonDatas = Lists.newArrayList();
            List<String> ids = Lists.newArrayList();

            for (EtlStatOrderInfo etlStatOrderInfo : etlStatOrderInfoList) {
                etlStatOrderInfo = this.getEtlStatOrderInfo(etlStatOrderInfo);
                jsonDatas = getJsonList(jsonDatas, etlStatOrderInfo);
                ids.add("" + etlStatOrderInfo.getId());
                if (jsonDatas.size() == Constant.INSERT_ES_SIZE) {
                    insertEsReplaceIndexBatch(_index, _type, jsonDatas, ids);
                    continue;
                }
            }
            logger.info("jsonDatas.size() : {}", jsonDatas.size());

            insertEsReplaceIndexBatch(_index, _type, jsonDatas, ids);

//        EsHandler.getInstance().getElasticSearchHandler().close();
        }

        logger.info("runImportStatOrderInfo end !");
    }

    private void insertEsReplaceIndexBatch(String _index, String _type, List<String> jsonDatas, List<String> ids) {
        try {
            EsHandler.getInstance().getElasticSearchHandler().replaceIndexBatch(_index, _type, jsonDatas, ids);
        } catch (Exception e) {
            logger.error("getElasticSearchHandler().replaceIndexBatch Exceprion : {}", e.getMessage());
        }
    }

    private EtlShopInfo getNewEtlShopInfo(EtlShopInfo etlShopInfo) {
        if (etlShopInfo.getLongitude() != null && etlShopInfo.getLatitude() != null) {
            etlShopInfo.setGeo(new Double[]{etlShopInfo.getLongitude(), etlShopInfo.getLatitude()});
        }
        if (etlShopInfo.getOrderAmount() == null || etlShopInfo.getOrderAmount() == 0l) {
            etlShopInfo.setLiveness("非活跃");
        } else {
            etlShopInfo.setLiveness("活跃");
        }
        if (etlShopInfo.getEnableBES() == null || etlShopInfo.getEnableBES() == 0l) {
            etlShopInfo.setS_enableBES("未开启蓝牙电子称");
        } else {
            etlShopInfo.setS_enableBES("开启蓝牙电子称");
        }
        if (etlShopInfo.getEnableCash() == null || etlShopInfo.getEnableCash() == 0l) {
            etlShopInfo.setS_enableCash("未开启现金支付");
        } else {
            etlShopInfo.setS_enableCash("开启现金支付");
        }
        return etlShopInfo;
    }

    /**
     * Although the code here has some redundancy same, but it is so, the changes have other (consider and discuss)
     * @param etlStatOrderInfo
     * @return
     */
    private EtlStatOrderInfo getEtlStatOrderInfo(EtlStatOrderInfo etlStatOrderInfo) {
        if (etlStatOrderInfo.getLongitude() != null && etlStatOrderInfo.getLatitude() != null) {
            etlStatOrderInfo.setGeo(new Double[]{etlStatOrderInfo.getLongitude(), etlStatOrderInfo.getLatitude()});
        }
        if (etlStatOrderInfo.getOrderAmount() == null || etlStatOrderInfo.getOrderAmount() == 0l) {
            etlStatOrderInfo.setLiveness("非活跃");
        } else {
            etlStatOrderInfo.setLiveness("活跃");
        }
        if (etlStatOrderInfo.getEnableBES() == null || etlStatOrderInfo.getEnableBES() == 0l) {
            etlStatOrderInfo.setS_enableBES("未开启蓝牙电子称");
        } else {
            etlStatOrderInfo.setS_enableBES("开启蓝牙电子称");
        }
        if (etlStatOrderInfo.getEnableCash() == null || etlStatOrderInfo.getEnableCash() == 0l) {
            etlStatOrderInfo.setS_enableCash("未开启现金支付");
        } else {
            etlStatOrderInfo.setS_enableCash("开启现金支付");
        }
        return etlStatOrderInfo;
    }

    private List<String> getJsonList(List<String> jsonDatas, Object object) {
        String strJsonData = null;
        try {
            strJsonData = JsonUtil.obj2Json(object);
        } catch (Exception e) {
            logger.error("JsonUtil.obj2Json Exception : {}", e.getMessage());
        }
        jsonDatas.add(strJsonData);
        return jsonDatas;
    }

}
