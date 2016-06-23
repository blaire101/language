package com.x.demo.model;

public class MicroPointInfo {

    private Long shopId;

    private String shopName;

    private String shopFullName;

    private String sessionId; // session_id

    private Long productId;

    private String productName;

    private String createDt;

    @Override
    public String toString() {
        return "MicroPointInfo{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopFullName='" + shopFullName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", createDt='" + createDt + '\'' +
                '}';
    }

    public MicroPointInfo() {
    }

    public MicroPointInfo(Long shopId, String shopName, String shopFullName, String sessionId, Long productId, String productName, String createDt) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopFullName = shopFullName;
        this.sessionId = sessionId;
        this.productId = productId;
        this.productName = productName;
        this.createDt = createDt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

}

