package com.x.dmt.model;


import java.math.BigDecimal;

public class EtlShopInfo {

    private Long id;
    private String name;
    private String fullName;
    private String address;
    private String category;

    private Double latitude;
    private Double longitude;
    private Double[] geo;

    private Integer enableBES;
    private Integer enableCash;

    private String s_enableCash;
    private String s_enableBES;
    private String liveness;

    private Long orderAmount;

    private BigDecimal transactionAmount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double[] getGeo() {
        return geo;
    }

    public void setGeo(Double[] geo) {
        this.geo = geo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getEnableCash() {
        return enableCash;
    }

    public void setEnableCash(Integer enableCash) {
        this.enableCash = enableCash;
    }

    public Integer getEnableBES() {
        return enableBES;
    }

    public void setEnableBES(Integer enableBES) {
        this.enableBES = enableBES;
    }

    public String getS_enableCash() {
        return s_enableCash;
    }

    public void setS_enableCash(String s_enableCash) {
        this.s_enableCash = s_enableCash;
    }

    public String getS_enableBES() {
        return s_enableBES;
    }

    public void setS_enableBES(String s_enableBES) {
        this.s_enableBES = s_enableBES;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getLiveness() {
        return liveness;
    }

    public void setLiveness(String liveness) {
        this.liveness = liveness;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
