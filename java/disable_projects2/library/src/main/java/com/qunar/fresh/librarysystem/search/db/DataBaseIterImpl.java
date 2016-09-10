package com.qunar.fresh.librarysystem.search.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.SQLExceptionSubclassTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.qunar.fresh.librarysystem.search.Closeableiterable;

/**
 * 
 * @author hang.gao
 * 
 */
public class DataBaseIterImpl implements DatabaseIter {

    private final Logger logger = LoggerFactory.getLogger(DataBaseIterImpl.class);

    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * 异常转换，用于将SQLException转换成DataAccessException
     */
    private SQLExceptionTranslator exceptionTranslator;

    public DataBaseIterImpl() {
        exceptionTranslator = new SQLExceptionSubclassTranslator();
    }

    @Override
    public <T> Closeableiterable<T> select(final String sql, final RowMapper<T> mapper, Object... objects) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql));
        Preconditions.checkNotNull(mapper);
        return new ResultSetIterable<T>(mapper, sql);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 查询数据库，迭代结果集
     * 
     * @author hang.gao
     * 
     * @param <T>
     */
    private final class ResultSetIterable<T> implements Closeableiterable<T> {
        private final RowMapper<T> mapper;
        private final String sql;
        private ResultSet resultSet;
        private Connection connection;
        private PreparedStatement statement;

        private ResultSetIterable(RowMapper<T> mapper, String sql, Object... objects) {
            this.mapper = mapper;
            this.sql = sql;
            Preconditions.checkArgument(!Strings.isNullOrEmpty(sql));
            Preconditions.checkNotNull(mapper);
            try {
                connection = dataSource.getConnection();
                logger.debug("查询数据库:{}", sql);
                statement = connection.prepareStatement(sql);
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                throw exceptionTranslator.translate("select db", sql, e);
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {

                /**
                 * ResultSet的行号
                 */
                private int row = 0;

                @Override
                public boolean hasNext() {
                    try {
                        boolean next = resultSet.next();
                        if (next) {
                            row++;
                        }
                        return next;
                    } catch (SQLException e) {
                        throw exceptionTranslator.translate("Result set error", sql, e);
                    }
                }

                @Override
                public T next() {
                    try {
                        return mapper.mapRow(resultSet, row);
                    } catch (SQLException e) {
                        throw exceptionTranslator.translate("Result set error", sql, e);
                    }
                }

                @Override
                public void remove() {
                }
            };
        }

        @Override
        public void close() {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw exceptionTranslator.translate("select db", sql, e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw exceptionTranslator.translate("select db", sql, e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw exceptionTranslator.translate("select db", sql, e);
                }
            }
        }
    }

}
