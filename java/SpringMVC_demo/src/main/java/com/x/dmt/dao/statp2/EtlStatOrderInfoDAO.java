package com.x.dmt.dao.statp2;

import com.x.dmt.model.EtlStatOrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtlStatOrderInfoDAO {

    List<EtlStatOrderInfo> getEtlStatOrderInfo(@Param("start_date") String startDate, @Param("end_date") String endDate);

}

