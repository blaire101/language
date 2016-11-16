package com.qunar.fresh.librarysystem.search;

import com.qunar.fresh.librarysystem.search.index.IndexUpdater.IndexExecutor;
import com.qunar.fresh.librarysystem.search.query.QueryParser;

/**
 * 执行搜索相关的代码
 * 
 * @author hang.gao
 * 
 */
public interface SearchExecutor {

    /**
     * 将数据添加到索引
     * 
     * @param task 更新索引的任务
     */
    public abstract void updateIndex(IndexUpdateTask task);

    /**
     * 初始化索引，创建初始索引
     * 
     * @param action 创建索引的对象
     */
    public abstract void init(IndexExecutor action);

    /**
     * 初始化，不创建索引
     */
    public abstract void init();

    /**
     * 搜索
     * 
     * @param keywords 关键词
     * @param start 返回结果的起始
     * @param resultCount 返回结果的数量
     * @param searchFields 搜索的域
     * @param docFields 返回的文档中的数据
     * @return 搜索结果
     */
    public abstract SearchResult search(String keywords, int start, int resultCount, String[] searchFields,
            String[] docFields);

    /**
     * 清理方法
     * 
     * @throws Exception
     */
    public void destroy() throws Exception;
    
    public QueryParser getQueryParser();
}