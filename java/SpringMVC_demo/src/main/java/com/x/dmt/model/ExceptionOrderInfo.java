package com.x.dmt.model;

import java.util.Date;

/**
 * Date : 2016-07-06
 */
public class ExceptionOrderInfo {

    private Long id;

    private Long shopId;

    private String shopName;

    private String shopFullName;

    private String exceptOrderNumber;

    private Integer exceptType;

    private String exceptDesc;

    private Date tradeTime;

    private Date syncTime;

    private Date createTime;

    private Integer alertType; // 1 sended

    private Integer statusType; // 1 warning 2 error

    private Integer checkType; // 1 vip 2 all


    public ExceptionOrderInfo() {
    }

    public ExceptionOrderInfo(Long id, Long shopId, String shopName, String shopFullName, String exceptOrderNumber, Integer exceptType, String exceptDesc, Date tradeTime, Date syncTime, Date createTime, Integer alertType, Integer statusType, Integer checkType) {
        this.id = id;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopFullName = shopFullName;
        this.exceptOrderNumber = exceptOrderNumber;
        this.exceptType = exceptType;
        this.exceptDesc = exceptDesc;
        this.tradeTime = tradeTime;
        this.syncTime = syncTime;
        this.createTime = createTime;
        this.alertType = alertType;
        this.statusType = statusType;
        this.checkType = checkType;
    }

    @Override
    public String toString() {
        return "ExceptionOrderInfo{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopFullName='" + shopFullName + '\'' +
                ", exceptOrderNumber='" + exceptOrderNumber + '\'' +
                ", exceptType=" + exceptType +
                ", exceptDesc='" + exceptDesc + '\'' +
                ", tradeTime=" + tradeTime +
                ", syncTime=" + syncTime +
                ", createTime=" + createTime +
                ", alertType=" + alertType +
                ", statusType=" + statusType +
                ", checkType=" + checkType +
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

    public String getExceptOrderNumber() {
        return exceptOrderNumber;
    }

    public void setExceptOrderNumber(String exceptOrderNumber) {
        this.exceptOrderNumber = exceptOrderNumber;
    }

    public Integer getExceptType() {
        return exceptType;
    }

    public void setExceptType(Integer exceptType) {
        this.exceptType = exceptType;
    }

    public String getExceptDesc() {
        return exceptDesc;
    }

    public void setExceptDesc(String exceptDesc) {
        this.exceptDesc = exceptDesc;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAlertType() {
        return alertType;
    }

    public void setAlertType(Integer alertType) {
        this.alertType = alertType;
    }

    public Integer getStatusType() {
        return statusType;
    }

    public void setStatusType(Integer statusType) {
        this.statusType = statusType;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }
}
