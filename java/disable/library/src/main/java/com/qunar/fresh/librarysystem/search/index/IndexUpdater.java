package com.qunar.fresh.librarysystem.search.index;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

/**
 * 索引更新器
 * 
 * @author hang.gao
 * 
 */
public interface IndexUpdater {

    /**
     * 创建索引
     * 
     * @param action 具体的创建索引的过程
     * @param mode 索引的打开模式
     */
    public void updateIndex(IndexExecutor action, OpenMode mode);

    /**
     * 具体的创建索引的功能，利用IndexWriter创建索引
     * 
     * @author hang.gao Initial Created at 2014年3月27日
     *         <p/>
     */
    public interface IndexExecutor {

        /**
         * 更新索引，具体的创建索引的内部与实现相关
         * 
         * @param indexWriter 写入索引的对象
         * @throws java.io.IOException
         */
        void onUpdate(IndexWriter indexWriter) throws IOException;
    }
}
