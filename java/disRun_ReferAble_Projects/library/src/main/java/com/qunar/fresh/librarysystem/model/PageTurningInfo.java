package com.qunar.fresh.librarysystem.model;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午3:10 To change this template use File | Settings |
 * File Templates.
 */
public class PageTurningInfo {
    private int totalCount;
    private int pageSize;
    private int currentPageNum;
    private int totalPage;

    public PageTurningInfo() {
    }

    public PageTurningInfo(int totalCount, int pageSize, int currentPageNum, int totalPage) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPageNum = currentPageNum;
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
