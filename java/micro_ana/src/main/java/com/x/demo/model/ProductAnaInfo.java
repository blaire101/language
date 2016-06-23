package com.x.demo.model;

import java.util.Date;

public class ProductAnaInfo {

    private Long id;
    private Long shopId;

    private Long productId;
    private String productName;

    private Long productPv;
    private Long productUv;

    private Date statDate;
    private Date createDate;

    public ProductAnaInfo() {
    }

    public ProductAnaInfo(Long shopId, Long productId, String productName, Long productPv, Long productUv, Date statDate, Date createDate) {
        this.id = null;
        this.shopId = shopId;
        this.productId = productId;
        this.productName = productName;
        this.productPv = productPv;
        this.productUv = productUv;
        this.statDate = statDate;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ProductAnaInfo{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPv=" + productPv +
                ", productUv=" + productUv +
                ", statDate=" + statDate +
                ", createDate=" + createDate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Long getProductPv() {
        return productPv;
    }

    public void setProductPv(Long productPv) {
        this.productPv = productPv;
    }

    public Long getProductUv() {
        return productUv;
    }

    public void setProductUv(Long productUv) {
        this.productUv = productUv;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

