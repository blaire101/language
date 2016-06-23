package com.qunar.fresh.librarysystem.model.enums;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-31 Time: 下午1:32 To change this template use File | Settings |
 * File Templates.
 */
public enum LibraryStatus {

    /**
     * 不可用
     */
    INVALID(0, "不可用"),
    /**
     * 图书馆可用
     */
    VALID(1, "图书馆可用");

    private int code;
    private String text;

    private static final Map<Integer, LibraryStatus> INTEGER_LIBRARY_STATUS_MAP;

    private LibraryStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer code() {
        return ordinal();
    }

    public String text() {
        return text;
    }

    public static LibraryStatus codeOf(int code) {
        return values()[code];
    }

    static {
        Map<Integer, LibraryStatus> map = Maps.newHashMap();
        for (LibraryStatus bookStatus : LibraryStatus.values()) {
            map.put(bookStatus.code(), bookStatus);
        }
        INTEGER_LIBRARY_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static LibraryStatus from(int code) {
        return INTEGER_LIBRARY_STATUS_MAP.get(code);
    }
}
