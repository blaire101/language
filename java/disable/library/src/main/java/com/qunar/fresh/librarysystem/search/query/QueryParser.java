package com.qunar.fresh.librarysystem.search.query;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.search.Query;

/**
 * 将查询解析成Query对象
 * 
 * @author hang.gao
 * 
 */
public interface QueryParser {

	/**
	 * 对用户输入分词，并得到Query对象
	 * 
	 * @param keywords
	 *            关键词
	 * @param searchFields
	 *            搜索的域
	 * @return Query对象，用于表示搜索
	 * @throws IOException
	 */
	public Query tokenQuery(String keywords, String... searchFields)
			throws IOException;

	/**
	 * 获取分词
	 * 
	 * @param keywords
	 * @return
	 * @throws IOException
	 */
	List<String> tokenString(String keywords, String field) throws IOException;

}
