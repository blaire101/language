package com.qunar.fresh.librarysystem.model;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-3 Time: 上午11:08 To change this template use File | Settings |
 * File Templates.
 */
public class ReminderInfo {

    private int reserveId;
    private String userRtx;
    private String bookName;
    private String bookId;
    private String libName;
    private String libDept;
    private int expiresDay;

    public int getExpiresDay() {
        return expiresDay;
    }

    public void setExpiresDay(int expiresDay) {
        this.expiresDay = expiresDay;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUserRtx() {
        return userRtx;
    }

    public void setUserRtx(String userRtx) {
        this.userRtx = userRtx;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getLibDept() {
        return libDept;
    }

    public void setLibDept(String libDept) {
        this.libDept = libDept;
    }

    public ReminderInfo() {
    }

}
