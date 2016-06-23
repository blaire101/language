package com.qunar.fresh.librarysystem.utils;

/**
 * 运行时常量
 * 
 * @author hang.gao
 * 
 */
public abstract class RuntimeConstants {

    /**
     * 禁止被继承
     */
    private RuntimeConstants() {
    }

    /**
     * 默认的图书借出的时长（天）
     */
    public static final int DEFAULT_BOOK_BORROW_DAY = 30;

    /**
     * 错误信息返回给浏览器时的contentType
     */
    public static final String ERROR_INFO_CONTENT_TYPE = "text/json";
}
