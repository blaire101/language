package com.qunar.fresh.librarysystem.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

/**
 * 更新索引的任务，一次索引的更新创建一个此接口的对象
 * 
 * @author hang.gao
 * 
 */
public interface IndexUpdateTask {

    /**
     * 执行任务
     * 
     * @throws IOException 更新索引时出现IO错误
     */
    void run() throws IOException;

    /**
     * 设置IndexWriter对象
     */
    void setIndexWriter(IndexWriter indexWriter);
}
