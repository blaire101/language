package com.x.dmt.dao.main;

import com.x.dmt.model.ExceptionOrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainExceptionOrderInfoDAO {

    List<ExceptionOrderInfo> getDelayExceptionOrderInfoList(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime, @Param("diff_time") Long diff);

    List<ExceptionOrderInfo> getSyncTimeExceptionOrderInfoList(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime);

    List<ExceptionOrderInfo> getPaidExceptionOrderInfoList(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime);

    List<ExceptionOrderInfo> getTransactionExceptionOrderInfoList(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime);

    List<ExceptionOrderInfo> getItemAmountExceptionOrderInfoList(@Param("shop_id") Long shopId, @Param("start_time") String startTime, @Param("end_time") String endTime);

}

