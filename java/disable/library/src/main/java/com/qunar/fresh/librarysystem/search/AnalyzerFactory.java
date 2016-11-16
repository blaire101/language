package com.qunar.fresh.librarysystem.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

/**
 * 工厂，用于生产Analyzer对象
 * 
 * @author hang.gao
 * 
 */
public interface AnalyzerFactory {

    /**
     * @param version Lucene的版本
     * @return Analyzer对象，不能为空
     */
    Analyzer getInstance(Version version);
}
