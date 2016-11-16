package com.qunar.fresh.librarysystem.search.query;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.search.AnalyzerFactory;
import com.qunar.fresh.librarysystem.utils.ChineseToPinyin;

/**
 * 查询解析的简单实现
 * 
 * @author hang.gao
 * 
 */
public class SimpleQueryParser implements QueryParser {

    /**
     * 分词器工厂
     */
    private AnalyzerFactory analyzerFactory;

    /**
     * Lucene的版本
     */
    private Version version;

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(SimpleQueryParser.class);

    public SimpleQueryParser(Version version, AnalyzerFactory analyzerFactory) {
        Preconditions.checkNotNull(version);
        this.version = version;
        this.analyzerFactory = analyzerFactory;
    }

    /**
     * 对用户输入分词，并得到Query对象
     * 
     * @param keywords 关键词
     * @param searchFields 搜索的域
     * @return Query对象，用于表示搜索
     * @throws IOException
     */
    public Query tokenQuery(String keywords, String... searchFields) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkArgument(searchFields.length > 0);
        BooleanQuery query = new BooleanQuery(true);
        for (String field : searchFields) {
            token(keywords, query, field);
            token(ChineseToPinyin.transform(keywords), query, field);
        }
        logger.debug(query.toString());
        return query;
    }

    @Override
	public List<String> tokenString(String keywords, String field) throws IOException {
    	Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
    	Preconditions.checkArgument(!Strings.isNullOrEmpty(field));
    	TokenStream tokenStream = null;
        Analyzer analyzer = null;
        List<String> list = Lists.newArrayList();
        list.add(keywords);
        try {
            analyzer = analyzerFactory.getInstance(version);
            tokenStream = analyzer.tokenStream(field, keywords);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String token = getTokenString(tokenStream);
                if (!Strings.isNullOrEmpty(token)) {
                	list.add(token);
                }
            }
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    logger.error("close token stream error", e);
                }
            }
            if (analyzer != null) {
                analyzer.close();
            }
        }
		return list;
	}

	private void token(String keywords, BooleanQuery query, String field) throws IOException {
        TokenStream tokenStream = null;
        Analyzer analyzer = null;
        try {
            analyzer = analyzerFactory.getInstance(version);
            tokenStream = analyzer.tokenStream(field, keywords);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String token = getTokenString(tokenStream);
                if (!Strings.isNullOrEmpty(token)) {
                    query.add(new TermQuery(new Term(field, token)), Occur.SHOULD);
                }
            }
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    logger.error("close token stream error", e);
                }
            }
            if (analyzer != null) {
                analyzer.close();
            }
        }
    }

    /**
     * 从TokenStream中抽取一个分词的片断
     * 
     * @param tokenStream TokenStream，表示一断分词
     * @return 分词后的字符串片断
     */
    private String getTokenString(TokenStream tokenStream) {
        Iterator<AttributeImpl> iter = tokenStream.getAttributeImplsIterator();
        while (iter.hasNext()) {
            AttributeImpl attributeImpl = iter.next();
            if (attributeImpl instanceof CharTermAttribute) {
                return attributeImpl.toString();
            }
        }
        return null;
    }

    public AnalyzerFactory getAnalyzerFactory() {
        return analyzerFactory;
    }

    public void setAnalyzerFactory(AnalyzerFactory analyzerFactory) {
        this.analyzerFactory = analyzerFactory;
    }

}
