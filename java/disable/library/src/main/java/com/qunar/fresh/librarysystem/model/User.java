package com.qunar.fresh.librarysystem.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:25 To change this template use File | Settings
 * | File Templates.
 */
public class User {

    private int id;

    private String userRtx;

    private String userName;

    private String userDept;

    private int borrowNum; // 已经借阅数量

    private int remainBrowNum; // 剩余可借阅数量

    private int browTotalNum; // 总共可借阅数量

    private int redecorateTotalNum; // 该图书馆可以续借的次数

    public int getRedecorateTotalNum() {
        return redecorateTotalNum;
    }

    public void setRedecorateTotalNum(int redecorateTotalNum) {
        this.redecorateTotalNum = redecorateTotalNum;
    }

    public int getBrowTotalNum() {
        return browTotalNum;
    }

    public void setBrowTotalNum(int browTotalNum) {
        this.browTotalNum = browTotalNum;
    }

    private int libId;

    private String libDept;

    private String libName;

    private String email;

    private String mobile;
    private List<BorrowedBookInfo> resultList; // 借书列表

    public User() {
        this.userName = "";
        this.libDept = "";
        this.libName = "";
        this.userDept = "";
        this.email = "";
        this.mobile = "";
        this.redecorateTotalNum = 0;
        this.resultList = Lists.newArrayList();
        this.borrowNum = 0;
        this.browTotalNum = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserRtx() {
        return userRtx;
    }

    public void setUseRtx(String user_rtx) {
        this.userRtx = user_rtx;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(int borrow_num) {
        this.borrowNum = borrow_num;
    }

    public int getRemainBrowNum() {
        return remainBrowNum;
    }

    public void setRemainBrowNum(int remainBrowNum) {
        this.remainBrowNum = remainBrowNum;
    }

    public void setUserRtx(String userRtx) {
        this.userRtx = userRtx;
    }

    public String getLibDept() {
        return libDept;
    }

    public void setLibDept(String libDept) {
        this.libDept = libDept;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public List<BorrowedBookInfo> getResultList() {
        return resultList;
    }

    public void setResultList(List<BorrowedBookInfo> resultList) {
        this.resultList = resultList;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResultList()).append("/").append(getUserName()).append("/").append(getUserDept()).append("/")
                .append(getEmail()).append("  ");
        return sb.toString();
    }
}
