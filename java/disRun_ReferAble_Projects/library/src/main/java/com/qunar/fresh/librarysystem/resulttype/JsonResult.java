package com.qunar.fresh.librarysystem.resulttype;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午4:07 To change this template use File | Settings |
 * File Templates.
 */

/**
 * 每个 data用dataJson封装一下 ，最后 要发送的时候 在用 statusJson封装一下
 */
public class JsonResult {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Object> jsonDataMap;

    public JsonResult() {
        jsonDataMap = Maps.newHashMap();
    }

    public JsonResult(boolean isDataValid) {
        jsonDataMap = Maps.newHashMap();
        jsonDataMap.put("ret", isDataValid);
    }

    public Map<String, Object> getJsonDataMap() {
        return jsonDataMap;
    }

    public void setJsonDataMap(Map<String, Object> jsonDataMap) {
        this.jsonDataMap = jsonDataMap;
    }

    public JsonResult put(String dataKey, Object data) {
        jsonDataMap.put(dataKey, data);
        return this;
    }

    public static Map<String, Object> dataJson(boolean isSucceed, String dataKey, Object data) {
        Map<String, Object> obj = Maps.newHashMap();
        obj.put("ret", isSucceed);
        obj.put(dataKey, data);
        return obj;
    }

    public static Map<String, Object> statusJson(int status, String errmsg, Object data) {
        Map<String, Object> obj = new LinkedHashMap<String, Object>();
        obj.put("status", status);
        obj.put("errmsg", errmsg);
        obj.put("data", data);

        return obj;
    }

    public static Map<String, Object> errInfo(String err) {
        Map<String, Object> obj = new LinkedHashMap<String, Object>();
        Map<String, Object> dataObj = new LinkedHashMap<String, Object>();
        obj.put("status", 0);
        obj.put("errmsg", "");

        dataObj.put("ret", false);
        dataObj.put("errInfo", err);
        obj.put("data", dataObj);
        return obj;
    }

    public static Map<String, Object> errInfo(String err, String objectString, Object object) {
        Map<String, Object> obj = new LinkedHashMap<String, Object>();
        Map<String, Object> dataObj = new LinkedHashMap<String, Object>();
        obj.put("status", 0);
        obj.put("errmsg", "");

        dataObj.put("ret", false);
        dataObj.put("errInfo", err);
        dataObj.put(objectString, object);
        obj.put("data", dataObj);
        return obj;
    }

}
