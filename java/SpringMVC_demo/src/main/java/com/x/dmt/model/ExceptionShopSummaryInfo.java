package com.x.dmt.model;

import java.util.Date;

/**
 * Date : 2016-07-19
 */
public class ExceptionShopSummaryInfo {

    private Long id;

    private Long shopId;

    private String shopName;

    private String shopFullName;

    private Integer exceptType;

    private String exceptDesc;

    private Date statDate;

    private Date createTime;

    private Integer alertType; // 1 sended

    private Integer statusType; // 1 warning 2 error

    private Integer checkType; // 1 vip 2 all

    public ExceptionShopSummaryInfo() {
    }

    public ExceptionShopSummaryInfo(Long id, Long shopId, String shopName, String shopFullName, Integer exceptType, String exceptDesc, Date statDate, Date createTime, Integer alertType, Integer statusType, Integer checkType) {
        this.id = id;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopFullName = shopFullName;
        this.exceptType = exceptType;
        this.exceptDesc = exceptDesc;
        this.statDate = statDate;
        this.createTime = createTime;
        this.alertType = alertType;
        this.statusType = statusType;
        this.checkType = checkType;
    }

    @Override
    public String toString() {
        return "ExceptionShopSummaryInfo{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopFullName='" + shopFullName + '\'' +
                ", exceptType=" + exceptType +
                ", exceptDesc='" + exceptDesc + '\'' +
                ", statDate=" + statDate +
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

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
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
