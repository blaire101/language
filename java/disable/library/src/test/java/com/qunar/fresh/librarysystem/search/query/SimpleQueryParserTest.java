package com.qunar.fresh.librarysystem.search.query;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Test;

import com.qunar.fresh.librarysystem.search.SmartChineseAnalyzerFactory;
import com.qunar.fresh.librarysystem.search.book.BookSearchEngine.IndexField;
import com.qunar.fresh.librarysystem.search.query.SimpleQueryParser;

/**
 * 测试查询解析器
 * 
 * @author hang.gao
 * 
 */
public class SimpleQueryParserTest {

	@Test
	public void testTokenQuery() throws IOException {
		SimpleQueryParser parser = new SimpleQueryParser(Version.LUCENE_47, new SmartChineseAnalyzerFactory());
		Query query = parser.tokenQuery("Thinking in Java", IndexField.BOOK_NAME.toString());
		Assert.assertEquals(query.toString(), "BOOK_NAME:think BOOK_NAME:in BOOK_NAME:java");
	}

    /**
     * 测试分词，输入关键词为空的情况
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTokenQuery_when_keywords_is_null() throws IOException {
        SimpleQueryParser parser = new SimpleQueryParser(Version.LUCENE_47, new SmartChineseAnalyzerFactory());
        parser.tokenQuery(null, IndexField.BOOK_NAME.toString());
    }

    /**
     * 测试分词，输入关键词为空的情况
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTokenQuery_when_keywords_is_empty() throws IOException {
        SimpleQueryParser parser = new SimpleQueryParser(Version.LUCENE_47, new SmartChineseAnalyzerFactory());
        parser.tokenQuery("", IndexField.BOOK_NAME.toString());
    }

    /**
     * 测试分词，输入关键词为空的情况
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTokenQuery_when_searchFields_is_empty() throws IOException {
        SimpleQueryParser parser = new SimpleQueryParser(Version.LUCENE_47, new SmartChineseAnalyzerFactory());
        parser.tokenQuery("Thinking in Java");
    }

}
