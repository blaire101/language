package com.x.dmt.model;


public class EtlStatOrderInfo extends StatOrderInfo {

    private String name;

    private String fullName;

    private String category;

    private String address;

    private String cityCode;

    private Double latitude;

    private Double longitude;

    private Double[] geo;

    private String liveness;

    private Integer enableCash;

    private Integer enableBES;

    private String s_enableCash;

    private String s_enableBES;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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

    public String getLiveness() {
        return liveness;
    }

    public void setLiveness(String liveness) {
        this.liveness = liveness;
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


}
