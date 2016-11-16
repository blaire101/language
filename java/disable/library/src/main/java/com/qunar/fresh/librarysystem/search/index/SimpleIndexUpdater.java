package com.qunar.fresh.librarysystem.search.index;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.base.Preconditions;
import com.qunar.fresh.librarysystem.search.AnalyzerFactory;
import com.qunar.fresh.librarysystem.search.SmartChineseAnalyzerFactory;

/**
 * IndexUpdater接口的简单实现
 * 
 * @author hang.gao
 * 
 */
public class SimpleIndexUpdater implements IndexUpdater {

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(SimpleIndexUpdater.class);

    /**
     * Lucene的版本
     */
    private Version version = Version.LUCENE_47;

    /**
     * 索引的目录
     */
    private Directory directory;

    /**
     * 分词器工厂
     */
    private AnalyzerFactory analyzerFactory;

    public SimpleIndexUpdater(Directory directory) {
        this(directory, null);
    }

    public SimpleIndexUpdater(Directory directory, AnalyzerFactory analyzerFactory) {
        Preconditions.checkNotNull(directory);
        this.directory = directory;
        if (analyzerFactory != null) {
            this.analyzerFactory = analyzerFactory;
        } else {
            this.analyzerFactory = getDefaultAnalyzerFactory();
        }
    }

    /**
     * 创建索引
     * 
     * @param action 具体的创建索引的过程
     * @param mode 索引的打开模式
     * @throws java.io.IOException IO错误
     */
    public void updateIndex(IndexExecutor action, OpenMode mode) {
        Preconditions.checkNotNull(action);
        Preconditions.checkNotNull(mode);
        IndexWriter indexWriter = null;
        Analyzer analyzer = null;
        try {
            IndexWriterConfig conf = new IndexWriterConfig(version, analyzer = analyzerFactory.getInstance(version));
            conf.setOpenMode(mode);
            indexWriter = new IndexWriter(directory, conf);
            action.onUpdate(indexWriter);
            indexWriter.commit();
        } catch (IOException e) {
            throw new ResourceAccessException("Index create error", e);
        } finally {
            try {
                if (indexWriter != null) {
                    indexWriter.close();
                }
            } catch (IOException e) {
                logger.error("Close IndexWriter failed", e);
            }
            if (analyzer != null) {
                analyzer.close();
            }
        }
    }

    /**
     * 
     * @return 获取默认分词器工厂
     */
    protected AnalyzerFactory getDefaultAnalyzerFactory() {
        return new SmartChineseAnalyzerFactory();
    }

}
