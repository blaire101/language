package com.x.demo.model;

import java.util.Set;

/**
 * Date : 2016-06-13
 */
public class TmpProductAnaInfo {

    private String productName;

    private Long productPv;

    private Set<String> sessionIdSet;

    public TmpProductAnaInfo() {
    }

    public TmpProductAnaInfo(String productName, Long productPv, Set<String> sessionId) {
        this.productName = productName;
        this.productPv = productPv;
        this.sessionIdSet = sessionId;
    }

    @Override
    public String toString() {
        return "TmpProductAnaInfo{" +
                "productName='" + productName + '\'' +
                ", productPv=" + productPv +
                ", sessionIdSet=" + sessionIdSet +
                '}';
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

    public Set<String> getSessionIdSet() {
        return sessionIdSet;
    }

    public void setSessionIdSet(Set<String> sessionIdSet) {
        this.sessionIdSet = sessionIdSet;
    }
}
