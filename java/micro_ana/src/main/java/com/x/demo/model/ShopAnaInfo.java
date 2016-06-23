package com.x.demo.model;

import java.util.Date;

public class ShopAnaInfo {

    private Long id;
    private Long shopId;

    private String shopName;
    private String shopFullName;

    private Long shopPv;
    private Long shopUv;

    private Date statDate;
    private Date createDate;

    public ShopAnaInfo() {
    }

    public ShopAnaInfo(Long shopId, String shopName, String shopFullName, Long shopPv, Long shopUv, Date statDate, Date createDate) {
        this.id = null;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopFullName = shopFullName;
        this.shopPv = shopPv;
        this.shopUv = shopUv;
        this.statDate = statDate;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ShopAnaInfo{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopFullName='" + shopFullName + '\'' +
                ", shopPv=" + shopPv +
                ", shopUv=" + shopUv +
                ", statDate=" + statDate +
                ", createDate=" + createDate +
                '}';
    }
}

