package com.qunar.fresh.librarysystem.search;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Test;

import com.qunar.fresh.librarysystem.search.book.BookSearchEngine.IndexField;
import com.qunar.fresh.librarysystem.search.index.IndexUpdater.IndexExecutor;
import com.qunar.fresh.librarysystem.search.query.QueryParser;

/**
 * 测试SearchExecutor类
 *
 * @author hang.gao
 */
public class LuceneExecutorTest {

    private IndexExecutor indexCreator = mock(IndexExecutor.class);

    private IndexUpdateTask indexUpdateTask = mock(IndexUpdateTask.class);

    private IndexSearcher indexSearcher = mock(IndexSearcher.class);

    private QueryParser queryParser = mock(QueryParser.class);

    /**
     * @author hang.gao
     */
    @Test
    public void testInit() {
        SearchExecutor executor = new LuceneExecutor(new RAMDirectory(), 1, null, null);
        executor.init(indexCreator);
    }

    /**
     * @author hang.gao
     */
    @Test
    public void testIndex() {
        SearchExecutor executor = new LuceneExecutor(new RAMDirectory(), 1, null, null);
        executor.updateIndex(indexUpdateTask);
    }

    /**
     * @author hang.gao
     */
    @Test(expected = NullPointerException.class)
    public void testIndex_when_book_is_null() {
        SearchExecutor executor = new LuceneExecutor(new RAMDirectory(), 1, null, null);
        executor.updateIndex(null);
    }

    /**
     * @throws IOException
     * @author hang.gao
     */
    @Test
    public void testSearch() throws IOException {
        TopDocs topDocs = mock(TopDocs.class);
        String[] fields = new String[]{IndexField.AUTHOR.toString()};
        topDocs.scoreDocs = new ScoreDoc[0];
        LuceneExecutor executor = new LuceneExecutor(new RAMDirectory(), 1, null, queryParser);
        Query query = new BooleanQuery(true);
        when(queryParser.tokenQuery("Java", fields)).thenReturn(query);
        when(indexSearcher.search(query, 10)).thenReturn(topDocs);
        executor.setIndexSearcher(indexSearcher);
        SearchResult searchResult = executor.search("Java", 0, 10, fields, fields);
        Assert.assertEquals(0, searchResult.getTotalBookCount());
        Assert.assertEquals(Collections.emptyList(), searchResult.getSearchResult());
    }

    /**
     * Query对象为null时，查询结果应该为<code>SearchResult.NONE</code>
     * @throws IOException
     * @author hang.gao
     */
    @Test
    public void testSearch_when_query_is_null() throws IOException {
        TopDocs topDocs = mock(TopDocs.class);
        String[] fields = new String[]{IndexField.AUTHOR.toString()};
        topDocs.scoreDocs = new ScoreDoc[0];
        LuceneExecutor executor = new LuceneExecutor(new RAMDirectory(), 1, null, queryParser);
        when(queryParser.tokenQuery("Java", fields)).thenReturn(null);
        SearchResult searchResult = executor.search("Java", 0, 10, fields, fields);
        Assert.assertEquals(SearchResult.NONE, searchResult);
    }

}
