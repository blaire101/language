package com.qunar.fresh.librarysystem.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用枚举处理
 * 
 * <p/>
 * 在mybatis-config.xml中配置如下:
 * 
 * <ul>
 * <li>必须指定javaType枚举类
 * <li>枚举类必须包含code和静态的codeOf方法
 * </ul>
 * 
 * <pre>
 * &lt;typeHandlers&gt;
 *     &lt;typeHandler javaType="com.qunar...XXXEnum" handler="com.qunar.base.meerkat.orm.mybatis.type.CodeEnumTypeHandler" /&gt;
 * &lt;/typeHandlers&gt;
 * </pre>
 * 
 * TypeHandler初始化参考{@link org.apache.ibatis.builder.xml.XMLConfigBuilder} 中方法typeHandlerElement
 * 
 * @author zhongyuan.zhang
 */
@SuppressWarnings("rawtypes")
public class CodeEnumTypeHandler extends BaseTypeHandler<Enum<?>> {

    private Method code;
    private Method codeOf;

    public CodeEnumTypeHandler(Class<Enum<?>> enumType) {

        String className = enumType.getName();
        String simpleName = enumType.getSimpleName();

        try {
            code = enumType.getDeclaredMethod("code");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method " + className + "#code():int required.'");
        }

        try {
            codeOf = enumType.getDeclaredMethod("codeOf", int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code):" + simpleName + " required.");
        }

        if (!Modifier.isStatic(codeOf.getModifiers())) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code):" + simpleName + " required.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, code(parameter));
    }

    @Override
    public Enum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return codeOf(rs.getInt(columnName));
    }

    @Override
    public Enum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return codeOf(rs.getInt(columnIndex));
    }

    @Override
    public Enum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return codeOf(cs.getInt(columnIndex));
    }

    private Integer code(Enum object) {
        try {
            return (Integer) code.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Enum codeOf(int value) {
        try {
            return (Enum) codeOf.invoke(null, value); // invoke static method
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}