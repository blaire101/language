package com.qunar.fresh.librarysystem.model;

import com.qunar.fresh.librarysystem.handler.EnumSerializer;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:11 To change this template use File | Settings
 * | File Templates.
 */

public class LogInfo {

    private OperationType operationType;

    private Date createTime;

    private String operatorRtx;

    private int libId;

    private String info;

    public LogInfo() {
    }

    public LogInfo(OperationType operationType, Date createTime, String operatorRtx, int libId, String info) {
        this.operationType = operationType;
        this.createTime = createTime;
        this.operatorRtx = operatorRtx;
        this.libId = libId;
        this.info = info;
    }

    public LogInfo(OperationType operationType, Date createTime, String operatorRtx, String info) {
        this.operationType = operationType;
        this.createTime = createTime;
        this.operatorRtx = operatorRtx;
        this.info = info;
    }

    public LogInfo(OperationType operationType, Date createTime, int libId) {
        this.operationType = operationType;
        this.createTime = createTime;
        this.libId = libId;
    }

    public LogInfo(LogInfo logInfo) {
        this.operationType = logInfo.getOperationType();
        this.createTime = logInfo.getCreateTime();
        this.operatorRtx = logInfo.getOperatorRtx();
        this.libId = logInfo.getLibId();
        this.info = logInfo.getInfo();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOperatorRtx() {
        return operatorRtx;
    }

    public void setOperatorRtx(String operatorRtx) {
        this.operatorRtx = operatorRtx;
    }

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = EnumSerializer.class)
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getInfo() {
        return info;
    }
}
