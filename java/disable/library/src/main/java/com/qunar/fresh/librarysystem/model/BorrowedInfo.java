package com.qunar.fresh.librarysystem.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-4-18 Time: 下午2:59 To change this template use File | Settings
 * | File Templates.
 */
public class BorrowedInfo {
    int totalCount;
    int pageSize;
    int currentPageNum;
    int totalPage;
    List<BorrowedBookInfo> resultList;

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

    public List<BorrowedBookInfo> getResultList() {
        return resultList;
    }

    public void setResultList(List<BorrowedBookInfo> resultList) {
        this.resultList = resultList;
    }
}
