package com.qunar.fresh.librarysystem.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.util.Version;

/**
 * 生产SmartChineseAnalyzer的工厂
 * 
 * @author hang.gao
 * 
 */
public class SmartChineseAnalyzerFactory implements AnalyzerFactory {

    @Override
    public Analyzer getInstance(Version version) {
        return new SmartChineseAnalyzer(version);
    }

}
