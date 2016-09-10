package com.x.dmt.dao.statp2;

import com.x.dmt.model.EtlShopInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtlShopInfoDAO {

    List<EtlShopInfo> getEtlShopInfo(@Param("end_date") String endDate);

}

