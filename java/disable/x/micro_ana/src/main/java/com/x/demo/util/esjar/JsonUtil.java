package com.x.demo.util.esjar;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class JsonUtil {

    /**
     * 实现将实体对象转换成json对象
     *
     * @param object 实体对象
     * @return
     */
    public static String obj2Json(Object object) throws IOException, IllegalAccessException {
        String jsonData = null;
        XContentBuilder jsonBuild = XContentFactory.jsonBuilder().startObject();
        Field[] fields = getAllField(object.getClass());
        for (Field field : fields) {
            if (!isNull(object, field)) {
                Object value = field.get(object);
                if (field.getType() == BigDecimal.class) {
                    value = Double.parseDouble("" + value);
                } else if (field.getType() == Date.class || field.getType() == Timestamp.class) {//
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0800'");
                    value = sdf.format(value);
                }
                jsonBuild.field(field.getName(), value);
            }
        }
        jsonData = jsonBuild.string();
        return jsonData;
    }

    /**
     * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
     *
     * @param clazz
     * @return Field数组
     */
    public static Field[] getAllField(Class<?> clazz) {
        ArrayList<Field> fieldList = new ArrayList<Field>();
        Field[] dFields = clazz.getDeclaredFields();
        if (null != dFields && dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Field[] superFields = getAllField(superClass);
            if (null != superFields && superFields.length > 0) {
                for (Field field : superFields) {
                    if (!isContain(fieldList, field)) {
                        fieldList.add(field);
                    }
                }
            }
        }
        Field[] result = new Field[fieldList.size()];
        fieldList.toArray(result);
        return result;
    }

    /**
     * 检测Field List中是否已经包含了目标field
     *
     * @param fieldList
     * @param field     带检测field
     * @return
     */
    public static boolean isContain(ArrayList<Field> fieldList, Field field) {
        for (Field temp : fieldList) {
            if (temp.getName().equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String map2Json(Map<String, Object> map) {
        String jsonData = null;
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = XContentFactory.jsonBuilder().startObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonBuild.field(entry.getKey(), entry.getValue());
            }
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            jsonBuild.close();
        }
        return jsonData;
    }

    private static boolean isNull(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object) == null;
        } catch (Exception e) {
            return false;
        }
    }
}
