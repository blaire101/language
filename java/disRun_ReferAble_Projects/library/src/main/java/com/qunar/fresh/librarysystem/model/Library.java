package com.qunar.fresh.librarysystem.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:05 To change this template use File | Settings
 * | File Templates.
 */
public class Library {
    private int libId;
    // 图书管名
    private String libName;
    // 图书馆部门
    private String libDept;

    // 管理员
    private List<Manager> libAdmin;
    // 数据库分类
    private Map<String, List<String>> libClass;
    // true false
    private boolean status;
    // 总数
    private int libBookNum;

    private Date createTime;

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public List<Manager> getLibAdmin() {
        return libAdmin;
    }

    public void setLibAdmin(List<Manager> libAdmin) {
        this.libAdmin = libAdmin;
    }

    public Map<String, List<String>> getLibClass() {
        return libClass;
    }

    public void setLibClass(Map<String, List<String>> libClass) {
        this.libClass = libClass;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getLibBookNum() {
        return libBookNum;
    }

    public void setLibBookNum(int libBookNum) {
        this.libBookNum = libBookNum;
    }

    public String getLibDept() {
        return libDept;
    }

    public void setLibDept(String libDept) {
        this.libDept = libDept;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
