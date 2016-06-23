package com.x.demo.model;

/**
 * Date : 2016-06-12
 */
public class ShopOpenMicroInfo {
    private Long shopId;
    private String shopName;
    private String shopFullName;

    public ShopOpenMicroInfo(Long shopId, String shopName, String shopFullName) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopFullName = shopFullName;
    }

    @Override
    public String toString() {
        return "ShopOpenMicroInfo{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopFullName='" + shopFullName + '\'' +
                '}';
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopFullName() {
        return shopFullName;
    }

    public void setShopFullName(String shopFullName) {
        this.shopFullName = shopFullName;
    }
}
