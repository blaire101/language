package com.qunar.fresh.librarysystem.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 需求：
 * enum转成json的时候，无论是使用默认的还是使用toString方法，都只能得到一个字符串;
 * 但是，有时候前端需要展示enum.value，传给后端时候需要使用enum.code，
 * 这时候把enum转成一个对象，并且包含enum里面的code & value，再传给前端这样会比较好。
 * 
 * 前提：
 * enum有如下两个方法
 * 1) int getCode()
 * 2) String getValue()
 * 
 * 使用方法：
 * 在enum的get方法上添加序列化标识：
 * <code>@JsonSerialize(using = EnumSerializer.class)</code>
 * </pre>
 * 
 * @author liandecai
 * @time Mar 19, 2014
 * 
 *       Modify by mengfan.feng
 */
public class EnumSerializer extends JsonSerializer<Enum<?>> {

    private Logger logger = LoggerFactory.getLogger(EnumSerializer.class);

    @Override
    public void serialize(Enum<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {

        try {
            Class<Enum<?>> clazz = (Class<Enum<?>>) value.getClass();
            Method getValue = clazz.getDeclaredMethod("text");

            String text = (String) getValue.invoke(value);
            jgen.writeString(text);
        } catch (NoSuchMethodException e) {
            logger.warn("EnumSerializer error: ", e);
        } catch (InvocationTargetException e) {
            logger.warn("EnumSerializer error: ", e);
        } catch (IllegalAccessException e) {
            logger.warn("EnumSerializer error: ", e);
        }
    }
}
