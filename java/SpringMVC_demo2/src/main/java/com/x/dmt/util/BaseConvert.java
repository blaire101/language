package com.x.dmt.util;

import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 2016-06-12
 */
public class BaseConvert {

    private BaseConvert() {
    }
    public static Map<String, Object> ConvertResultMap(ResultSet rs) throws SQLException {

        Map<String, Object> hm = Maps.newHashMap();

        ResultSetMetaData rsmd = rs.getMetaData();

        int count = rsmd.getColumnCount();

        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnLabel(i);
            Object value = rs.getObject(i);
            if (null != value) {
                hm.put(key, value);
            }
        }
        return hm;
    }
}
