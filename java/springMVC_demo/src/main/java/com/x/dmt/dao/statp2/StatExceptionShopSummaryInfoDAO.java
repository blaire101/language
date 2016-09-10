package com.x.dmt.dao.statp2;

import com.x.dmt.model.ExceptionShopSummaryInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatExceptionShopSummaryInfoDAO {

    List<ExceptionShopSummaryInfo> getExceptionShopSummaryInfo(@Param("stat_date") String statDate);

}

