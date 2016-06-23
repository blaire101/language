package com.x.demo.dao;

import com.x.demo.model.ProductAnaInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAnaInfoDAO {
    void addProductAnaInfo(ProductAnaInfo productAnaInfo);
    void addProductAnaInfoList(List<ProductAnaInfo> productAnaInfos);
}

