package com.qunar.fresh.librarysystem.model.enums;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:15 To change this template use File | Settings
 * | File Templates.
 */
public enum OperationType {

    /**
     * 借书
     */
    BORROWBOOK(0, "借书"),

    /**
     * 还书
     */
    RETURNBOOK(1, "还书"),

    /**
     * 变更书籍
     */
    ALTERBOOK(2, "变更书籍"),

    /**
     * 删除书籍
     */
    DELETEBOOK(3, "删除书籍"),

    ADDLIBRARY(4, "增加图书馆"), DELETELIBRARY(5, "删除图书馆"),

    ADDADMIN(6, "增加管理员"), DELETEADMIN(7, "删除管理员"),
    /**
     * 系统设置
     */
    SYSTEMSETTING(8, "系统设置"), OTHER(9, "其他操作");

    private int code;
    private String text;

    private OperationType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer code() {
        return ordinal();
    }

    public String text() {
        return text;
    }

    public static OperationType codeOf(int code) {
        return values()[code];
    }
}
