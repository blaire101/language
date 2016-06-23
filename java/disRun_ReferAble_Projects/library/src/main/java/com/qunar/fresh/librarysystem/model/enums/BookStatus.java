package com.qunar.fresh.librarysystem.model.enums;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-27 Time: 下午6:22 To change this template use File | Settings
 * | File Templates.
 */
public enum BookStatus {
    INLIBRARY(0, "在库"), BORROWED(1, "借出"), DELETED(2, "删除");
    private int code;
    private String text;

    private BookStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer code() {
        return code;
    }

    public static BookStatus codeOf(int code) {
        return INTEGER_BOOK_STATUS_MAP.get(code);
    }

    private static final Map<Integer, BookStatus> INTEGER_BOOK_STATUS_MAP;

    static {
        Map<Integer, BookStatus> map = Maps.newHashMap();
        for (BookStatus bookStatus : BookStatus.values()) {
            map.put(bookStatus.code(), bookStatus);
        }
        INTEGER_BOOK_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static BookStatus from(int code) {
        return INTEGER_BOOK_STATUS_MAP.get(code);
    }
}
