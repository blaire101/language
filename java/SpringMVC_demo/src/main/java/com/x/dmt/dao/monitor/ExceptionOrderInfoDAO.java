package com.x.dmt.dao.monitor;

import com.x.dmt.model.ExceptionOrderInfo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExceptionOrderInfoDAO { // shop_exception_order

    void addExceptionOrderInfoList(List<ExceptionOrderInfo> exceptionOrderInfoList);

    List<ExceptionOrderInfo> getExceptionOrderInfoListByShopTimeAlertStatusCheck(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime, @Param("alert_type")  Integer alertType, @Param("status_type")  Integer statusType, @Param("check_type") Integer checkType);

    List<ExceptionOrderInfo> getExceptionOrderInfoListByShopTimeCheck(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime, @Param("check_type") Integer checkType);

    void updateExceptionOrderInfoListAlertTypeByAlertCheck(@Param("alert_type") Integer alertType, @Param("alert_condition") Integer alertCondition, @Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime, @Param("check_type") Integer checkType);

}

