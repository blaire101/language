package com.x.dmt.model;
/**
 * Date : 2016-07-05
 */
public class ShopIdInfo {

    private Long shopId;

    public ShopIdInfo() {
    }


    public ShopIdInfo(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "VipShopInfo{" +
                "shopId=" + shopId +
                '}';
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
