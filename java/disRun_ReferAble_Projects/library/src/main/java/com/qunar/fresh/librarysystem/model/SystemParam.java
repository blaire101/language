package com.qunar.fresh.librarysystem.model;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:34 To change this template use File | Settings
 * | File Templates.
 */
public class SystemParam {
    // 借阅总数
    private int borrowTotalNum;

    // 借阅期限
    private int borrowPeriod;

    // 续借次数
    private int redecorateNum;

    // 续借期限
    private int redecoratePeriod;

    // 提醒时间
    private int remindDay;

    private int libId;

    public int getLibid() {
        return libId;
    }

    public void setLibid(int libId) {
        this.libId = libId;
    }

    public int getBorrowTotalNum() {
        return borrowTotalNum;
    }

    public void setBorrowTotalNum(int borrowTotalNum) {
        this.borrowTotalNum = borrowTotalNum;
    }

    public int getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(int borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public int getRedecorateNum() {
        return redecorateNum;
    }

    public void setRedecorateNum(int redecorateNum) {
        this.redecorateNum = redecorateNum;
    }

    public int getRedecoratePeriod() {
        return redecoratePeriod;
    }

    public void setRedecoratePeriod(int redecoratePeriod) {
        this.redecoratePeriod = redecoratePeriod;
    }

    public int getRemindDay() {
        return remindDay;
    }

    public void setRemindDay(int remindDay) {
        this.remindDay = remindDay;
    }

    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (this.getClass() == object.getClass()) {
            SystemParam SysObject = (SystemParam) object;
            if (this.getRedecorateNum() != SysObject.getRedecorateNum()) {
                return false;
            }
            if (this.getRedecoratePeriod() != SysObject.getRedecoratePeriod()) {
                return false;
            }
            if (this.getBorrowPeriod() != SysObject.getBorrowPeriod()) {
                return false;
            }
            if (this.getBorrowTotalNum() != SysObject.getBorrowTotalNum()) {
                return false;
            }
            if (this.getRemindDay() != SysObject.getRemindDay()) {
                return false;
            }
        }
        return true;
    }
}
