package com.x.demo.dao;

import com.x.demo.model.ShopAnaInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopAnaInfoDAO {
    void addShopAnaInfo(ShopAnaInfo shopAnaInfo);
    void addShopAnaInfoList(List<ShopAnaInfo> shopAnaInfos);
}

