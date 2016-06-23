package com.x.demo.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {

    private static JDBCUtil jdbcUtilSingle = null;

    public static JDBCUtil getInitJDBCUtil() {
        if (jdbcUtilSingle == null) {
            // 给类加锁 防止线程并发
            synchronized (JDBCUtil.class) {
                if (jdbcUtilSingle == null) {
                    jdbcUtilSingle = new JDBCUtil();
                }
            }
        }
        return jdbcUtilSingle;
    }

    public JDBCUtil() {
    }

    // 关闭连接
    public static void closeConnection(ResultSet rs, Statement statement, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
