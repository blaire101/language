package com.qunar.fresh.librarysystem.search.book;

import com.qunar.fresh.librarysystem.search.SearchExecutor;
import com.qunar.fresh.librarysystem.search.SearchResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hang.gao
 * Date: 14-4-9
 * Time: 下午6:57
 *
 * @author hang.gao
 */
public class DatabaseBookSearchEngineTest {

    /**
     * 测试搜索结果为null的情况
     *
     * @author hang.gao
     */
    @Test
    public void testSearch_when_search_result_is_null() {
        String[] fields = new String[]{BookSearchEngine.IndexField.BOOK_NAME.toString()};
        DatabaseBookSearchEngine searchEngine = new DatabaseBookSearchEngine();
        SearchExecutor searchExecutor = mock(SearchExecutor.class);
        when(searchExecutor.search("Java", 1, 10, fields, fields)).thenReturn(null);
        searchEngine.setSearchExecutor(searchExecutor);
        searchEngine.setSearchFields(fields);
        searchEngine.setDocFields(fields);
        assertEquals(SearchResult.NONE, searchEngine.searchBook("Java", 1, 10));
    }

    /**
     * 测试搜索结果为空集合的情况
     *
     * @author hang.gao
     */
    @Test
    public void testSearch_when_search_result_is_empty() {
        String[] fields = new String[]{BookSearchEngine.IndexField.BOOK_NAME.toString()};
        DatabaseBookSearchEngine searchEngine = new DatabaseBookSearchEngine();
        SearchExecutor searchExecutor = mock(SearchExecutor.class);
        when(searchExecutor.search("Java", 1, 10, fields, fields)).thenReturn(SearchResult.NONE);
        searchEngine.setSearchExecutor(searchExecutor);
        searchEngine.setSearchFields(fields);
        searchEngine.setDocFields(fields);
        assertEquals(SearchResult.NONE, searchEngine.searchBook("Java", 1, 10));
    }

    /**
     * 测试搜索结果不为空集合也不为null
     *
     * @author hang.gao
     */
    @Test
    public void testSearch_when_search_result_is_not_empty() {
        String[] fields = new String[]{BookSearchEngine.IndexField.BOOK_NAME.toString()};
        DatabaseBookSearchEngine searchEngine = new DatabaseBookSearchEngine();
        SearchExecutor searchExecutor = mock(SearchExecutor.class);
        List<String> list = Arrays.asList("Thinking in Java", "Effective Java");
        SearchResult searchResult = new SearchResult(2, list);
        when(searchExecutor.search("Java", 1, 10, fields, fields)).thenReturn(searchResult);
        searchEngine.setSearchExecutor(searchExecutor);
        searchEngine.setSearchFields(fields);
        searchEngine.setDocFields(fields);
        assertEquals(searchResult, searchEngine.searchBook("Java", 1, 10));
    }
}
