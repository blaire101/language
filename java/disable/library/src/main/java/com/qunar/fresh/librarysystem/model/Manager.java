package com.qunar.fresh.librarysystem.model;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:32 To change this template use File | Settings
 * | File Templates.
 */
public class Manager {

    private String userRtx;

    private String userName;

    private int libId;

    private int status;

    private int isSuper;

    public Manager() {

    }

    public Manager(String userRtx, int libId) {
        this.userRtx = userRtx;
        this.libId = libId;
        this.userName = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRtx() {

        return userRtx;
    }

    public void setUserRtx(String userRtx) {
        this.userRtx = userRtx;
    }

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSuper() {
        return isSuper;
    }

    public void setSuper(int aSuper) {
        isSuper = aSuper;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(userRtx).append("/").append(userName).append("/").append(libId).append("/").append(status)
                .append("/").append(isSuper);
        return sb.toString();
    }
}
