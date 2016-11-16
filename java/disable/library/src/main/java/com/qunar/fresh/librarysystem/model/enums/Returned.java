package com.qunar.fresh.librarysystem.model.enums;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-28 Time: 下午12:00 To change this template use File | Settings
 * | File Templates.
 */
public enum Returned {
    NOTRETURNED(0, "未还"), ISRETURNED(1, "已还");
    private String text;
    private int code;

    private Returned(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer code() {
        return code;
    }

    public String text() {
        return text;
    }

    public static Returned codeOf(int code) {
        return INTEGER_RETURNED_STATUS_MAP.get(code);
    }

    private static final Map<Integer, Returned> INTEGER_RETURNED_STATUS_MAP;

    static {
        Map<Integer, Returned> map = Maps.newHashMap();
        for (Returned returned : Returned.values()) {
            map.put(returned.code(), returned);
        }
        INTEGER_RETURNED_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static Returned from(int code) {
        return INTEGER_RETURNED_STATUS_MAP.get(code);
    }
}
