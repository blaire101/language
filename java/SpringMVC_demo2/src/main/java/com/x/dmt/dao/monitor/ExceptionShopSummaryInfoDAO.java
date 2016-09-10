package com.x.dmt.dao.monitor;

import com.x.dmt.model.ExceptionShopSummaryInfo;
import com.x.dmt.model.ShopIdInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExceptionShopSummaryInfoDAO { // shop_exception_summary

    List<ExceptionShopSummaryInfo> getExceptionShopSummaryInfoListByStatDateCheckType(@Param("stat_date") String statDate, @Param("check_type") Integer checkType);

    void addExceptionShopSummaryInfoList(List<ExceptionShopSummaryInfo> exceptionShopSummaryInfoList);

    void updateExceptionShopSummaryInfoListStatusType(@Param("status_type") Integer statusType, @Param("id") Long id);

    List<ShopIdInfo> getExceptionShopIdInfos(@Param("stat_date") String statDate, @Param("check_type") Integer checkType, @Param("status_type") Integer statusType);

}

