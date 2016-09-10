package com.x.dmt.dao.monitor;

import com.x.dmt.model.ShopIdInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VipShopIdInfoDAO {  // monitor_shop table

    List<ShopIdInfo> getVipShopIdInfos();

}

