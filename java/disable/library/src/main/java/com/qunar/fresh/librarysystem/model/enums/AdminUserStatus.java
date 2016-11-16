package com.qunar.fresh.librarysystem.model.enums;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-4-2 Time: 下午5:26 To change this template use File | Settings |
 * File Templates.
 */
public enum AdminUserStatus {
    NOTVALID(0, "无效用户"), ISVALID(1, "有效用户");

    private int code;
    private String text;

    private AdminUserStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer code() {
        return code;
    }

    public static AdminUserStatus codeOf(int code) {
        return INTEGER_ADMIN_USER_STATUS_MAP.get(code);
    }

    private static final Map<Integer, AdminUserStatus> INTEGER_ADMIN_USER_STATUS_MAP;

    static {
        Map<Integer, AdminUserStatus> map = Maps.newHashMap();
        for (AdminUserStatus adminUserStatus : AdminUserStatus.values()) {
            map.put(adminUserStatus.code(), adminUserStatus);
        }
        INTEGER_ADMIN_USER_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static AdminUserStatus from(int code) {
        return INTEGER_ADMIN_USER_STATUS_MAP.get(code);
    }
}
