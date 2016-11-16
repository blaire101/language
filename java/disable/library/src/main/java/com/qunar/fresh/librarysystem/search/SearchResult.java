package com.qunar.fresh.librarysystem.search;

import java.util.Collections;
import java.util.List;

/**
 * 搜索结果
 * <p/>
 * 
 * @author hang.gao Initial Created at 2014年3月27日
 *         <p/>
 */
public final class SearchResult {

    public static final SearchResult NONE = new SearchResult(0, Collections.<String> emptyList());

    private final long totalBookCount;

    /**
     * 查询到的文本内容
     */
    private final List<String> searchResult;

    public SearchResult(long totalBookCount, List<String> searchResult) {
        super();
        this.totalBookCount = totalBookCount;
        this.searchResult = searchResult;
    }

    public long getTotalBookCount() {
        return totalBookCount;
    }

    public List<String> getSearchResult() {
        return searchResult;
    }

    @Override
    public String toString() {
        return "SearchResult [totalBookCount=" + totalBookCount + ", searchResult=" + searchResult + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((searchResult == null) ? 0 : searchResult.hashCode());
        result = prime * result + (int) (totalBookCount ^ (totalBookCount >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchResult other = (SearchResult) obj;
        if (searchResult == null) {
            if (other.searchResult != null)
                return false;
        } else if (!searchResult.equals(other.searchResult))
            return false;
        if (totalBookCount != other.totalBookCount)
            return false;
        return true;
    }

}
