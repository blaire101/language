package com.qunar.fresh.librarysystem.search.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.search.Closeableiterable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 测试DataBaseIterImpl
 * 
 * @author hang.gao
 * 
 */
public class DataBaseIterImplTest {

	private final Logger logger = LoggerFactory
			.getLogger(DataBaseIterImplTest.class);

	@Test
	public void test_select() throws SQLException {
		DataBaseIterImpl databaseIter = new DataBaseIterImpl();
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(dataSource.getConnection()).thenReturn(connection);
		String sql = "select * from book_info";
		when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
		ResultSet resultSet = mock(ResultSet.class);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true)
				.thenReturn(true).thenReturn(false);
		databaseIter.setDataSource(dataSource);
		Closeableiterable<Book> iter = null;
		try {
			iter = databaseIter.select(sql, new RowMapper<Book>() {

				@Override
				public Book mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Book book = new Book();
					book.setBookName(rs.getString("book_name"));
					return book;
				}
			});
			int i = 0;
			for (Book book : iter) {
				i++;
				logger.info(book.toString());
			}
			assertEquals(3, i);
		} finally {
			if (iter != null) {
				iter.close();
			}
		}
	}

	/**
	 * 测试，当查询的结果集为null时
	 * 
	 * @author hang.gao
	 * @throws SQLException
	 */
	@Test
	public void test_select_when_result_set_is_null() throws SQLException {
		DataBaseIterImpl databaseIter = new DataBaseIterImpl();
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(dataSource.getConnection()).thenReturn(connection);
		String sql = "select * from book_info";
		when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
		ResultSet resultSet = mock(ResultSet.class);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		databaseIter.setDataSource(dataSource);
		Closeableiterable<Book> iter = null;
		try {
			iter = databaseIter.select(sql, new RowMapper<Book>() {

				@Override
				public Book mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Book book = new Book();
					book.setBookName(rs.getString("book_name"));
					return book;
				}
			});
			int i = 0;
			for (Book book : iter) {
				i++;
				logger.info(book.toString());
			}
			assertEquals(0, i);
		} finally {
			if (iter != null) {
				iter.close();
			}
		}
	}
}
