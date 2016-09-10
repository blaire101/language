package com.qunar.fresh.librarysystem.search;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.Subscribe;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.qunar.fresh.librarysystem.search.index.IndexUpdater;
import com.qunar.fresh.librarysystem.search.index.IndexUpdater.IndexExecutor;
import com.qunar.fresh.librarysystem.search.index.SimpleIndexUpdater;
import com.qunar.fresh.librarysystem.search.query.QueryParser;
import com.qunar.fresh.librarysystem.search.query.SimpleQueryParser;

/**
 * 搜索功能的具体执行者
 * 
 * @author hang.gao
 * 
 */
public final class LuceneExecutor implements SearchExecutor, DisposableBean {

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(LuceneExecutor.class);

    /**
     * lucene的版本
     */
    private final Version version = Version.LUCENE_47;

    /**
     * 搜索器
     */
    private volatile IndexSearcher indexSearcher;

    /**
     * 索引的读取器
     */
    volatile IndexReader indexReader;

    /**
     * 索引的目录
     */
    private final Directory directory;

    /**
     * 阻塞队列，用于存储需要更新索引的任务
     */
    private final BlockingQueue<IndexUpdateTask> taskQueue;

    /**
     * 对象是否已经初始化
     */
    private volatile boolean init = false;

    /**
     * 分词器工厂
     */
    private AnalyzerFactory analyzerFactory;

    /**
     * 查询解析器
     */
    private QueryParser queryParser;

    /**
     * 索引的更新
     */
    private IndexUpdater indexUpdater;

    private EventBus eventBus;

    public LuceneExecutor(Directory directory, int queueSize, AnalyzerFactory analyzerFactory, QueryParser queryParser) {
        Preconditions.checkArgument(directory != null && queueSize > 0);
        this.directory = directory;
        this.queryParser = queryParser;
        if (analyzerFactory == null) {
            this.analyzerFactory = registryDefaultAnalyzerFactory();
        } else {
            this.analyzerFactory = analyzerFactory;
        }
        eventBus = new EventBus();
        taskQueue = new EventBlockingQueue<IndexUpdateTask>(new ArrayBlockingQueue<IndexUpdateTask>(queueSize),
                eventBus);
    }

    /**
     * 注册分词器工厂
     * 
     * @return 默认的分词器工厂
     */
    protected AnalyzerFactory registryDefaultAnalyzerFactory() {
        return new SmartChineseAnalyzerFactory();
    }

    @Override
    public void updateIndex(IndexUpdateTask task) {
        Preconditions.checkNotNull(task);
        taskQueue.add(task);
    }

    @Override
    public void init(IndexExecutor indexCreator) {
        if (!init) {
            synchronized (this) {
                if (!init) {
                    initialClassProperties();
                    if (indexCreator != null) {
                        eventBus.post(indexCreator);
                    }
                    refreshIndexSearcher();
                }
            }
        }
    }

    @Override
    public void init() {
        if (!init) {
            synchronized (this) {
                if (!init) {
                    initialClassProperties();
                }
                refreshIndexSearcher();
            }
        }
    }

    private void initialClassProperties() {
        init = true;
        if (indexUpdater == null) {
            indexUpdater = getDefaultIndexUpdater();
        }
        if (queryParser == null) {
            queryParser = getDefaultQueryParser();
        }
        if (indexUpdater == null) {
            throw new RuntimeException("must initial the index updater");
        }
        if (queryParser == null) {
            throw new RuntimeException("must initial the query parser");
        }
        eventBus.register(new IndexUpdateListener(indexUpdater, new IndexUpdateListener.IndexUpdateCallback() {

            @Override
            public void afterUpdate() {
                refreshIndexSearcher();
            }
        }));
    }

    /**
     * 返回默认的查询解析器，当构造器中传入的QueryParser为空，同时setter方法没有被调用时，调用此方法
     * 
     * @return 默认的查询解析器，不能为空
     */
    protected QueryParser getDefaultQueryParser() {
        return new SimpleQueryParser(version, analyzerFactory);
    }

    /**
     * 创建分词器
     * 
     * @return 分词器的对象
     */
    protected Analyzer newAnalyzer() {
        return analyzerFactory.getInstance(version);
    }

    @Override
    public SearchResult search(String keywords, int start, int resultCount, String[] searchFields, String[] docFields) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkArgument(start >= 0 && resultCount >= 1);
        Preconditions.checkNotNull(searchFields);
        Preconditions.checkNotNull(docFields);
        try {
            Query query = getTokenQuery(keywords, searchFields);
            if (query == null) {
                return SearchResult.NONE;
            }
            logger.debug("分词结果：{}", query.toString());
            TopDocs topDocs = indexSearcher.search(query, start + resultCount);
            logger.debug("搜索结果数量：{}", topDocs.totalHits);
            List<String> result = extractSearchResultString(start, resultCount, topDocs, docFields);
            logger.debug("Search result is {}", result);
            return new SearchResult(topDocs.totalHits, result);
        } catch (IOException e) {
            logger.error("Search failed with the keywords '{}'", keywords);
            throw new ResourceAccessException("The index search failed", e);
        }
    }

    /**
     * 抽取搜索结果
     * 
     * @param start
     * @param resultCount
     * @param topDocs
     * @param fields
     * @return
     * @throws IOException
     */
    private List<String> extractSearchResultString(int start, int resultCount, TopDocs topDocs, String... fields)
            throws IOException {
        List<String> result = Lists.newArrayList();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = start; i < start + resultCount && i < topDocs.totalHits; i++) {
            logger.debug(indexSearcher.doc(i).toString());
            for (String field : fields) {
                result.add(indexSearcher.doc(scoreDocs[i].doc).get(field));
            }
        }
        return result;
    }

    /**
     * 对用户输入分词，并得到Query对象
     * 
     * @param keywords 关键词
     * @param searchFields 搜索的域
     * @return Query对象，用于表示搜索
     * @throws IOException
     */
    private Query getTokenQuery(String keywords, String... searchFields) throws IOException {
        return queryParser.tokenQuery(keywords, searchFields);
    }

    public IndexUpdater getIndexUpdater() {
        return indexUpdater;
    }

    public void setIndexUpdater(IndexUpdater indexUpdater) {
        this.indexUpdater = indexUpdater;
    }

    public QueryParser getQueryParser() {
        return queryParser;
    }

    public void setQueryParser(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    /**
     * 返回索引更新器，返回结果不能为空
     */
    protected IndexUpdater getDefaultIndexUpdater() {
        return new SimpleIndexUpdater(directory, analyzerFactory);
    }

    IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public void setIndexSearcher(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }

    /**
     * 替换原有的IndexSearcher
     */
    private void refreshIndexSearcher() {
        IndexReader old = indexReader;
        try {
            if (old != null) {
                indexReader = DirectoryReader.openIfChanged((DirectoryReader) old);
                if (indexReader == null) {
                    indexReader = old;
                    old = null;
                }
            } else {
                indexReader = DirectoryReader.open(directory);
            }
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            throw new ResourceAccessException("Open index error " + indexReader, e);
        } finally {
            if (old != null) {
                try {
                    old.close();
                } catch (IOException e) {
                    logger.error("index reader close error {}", e);
                }
            }
        }
    }

    /**
     * 关闭IndexReader
     */
    @Override
    public void destroy() throws Exception {
        if (indexReader != null) {
            indexReader.close();
        }
    }

    /**
     * 监听队列是否已经满了
     * 
     * @author hang.gao
     * 
     */
    private static final class IndexUpdateListener {

        /**
         * 异步执行任务使用的执行器
         */
        private ExecutorService executorService;

        /**
         * 日志
         */
        private Logger logger = LoggerFactory.getLogger(IndexUpdateListener.class);

        /**
         * 索引更新器
         */
        private IndexUpdater indexUpdater;

        /**
         * 更新完成后调用的方法
         */
        private IndexUpdateCallback indexUpdateCallback;

        IndexUpdateListener(IndexUpdater indexUpdater, IndexUpdateCallback action) {
            Preconditions.checkNotNull(indexUpdater);
            this.indexUpdater = indexUpdater;
            this.indexUpdateCallback = action;
            executorService = Executors.newSingleThreadExecutor();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                @Override
                public void run() {
                    // 关闭线程池
                    executorService.shutdown();
                    logger.debug("Close the executorService on hook");
                }
            }));
        }

        /**
         * 当队列满时，执行此方法
         * 
         * @param queue 满了的队列
         */
        @Subscribe
        public void onBlockingQueueFull(BlockingQueue<IndexUpdateTask> queue) {
            executorService.execute(new IndexUpdateInvoker(queue));
        }

        /**
         * 初始化索引事件
         * 
         * @param indexExecutor 索引的创建者
         */
        @Subscribe
        public void onInit(IndexExecutor indexExecutor) {
            updateIndex(indexExecutor, IndexWriterConfig.OpenMode.CREATE);
        }

        private Object[] cleanQueue(BlockingQueue<?> queue) {
            Object[] elems = queue.toArray();
            // 清空队列
            queue.clear();
            return elems;
        }

        /**
         * 添加索引的任务
         * 
         * @author hang.gao
         * 
         */
        private final class IndexUpdateInvoker implements Runnable {

            private final BlockingQueue<IndexUpdateTask> queue;

            private IndexUpdateInvoker(BlockingQueue<IndexUpdateTask> queue) {
                this.queue = queue;
            }

            @Override
            public void run() {
                updateIndex(new IndexExecutor() {

                    @Override
                    public void onUpdate(IndexWriter indexWriter) throws IOException {
                        Object[] elems = cleanQueue(queue);
                        for (Object e : elems) {
                            IndexUpdateTask task = (IndexUpdateTask) e;
                            task.setIndexWriter(indexWriter);
                            task.run();
                        }
                    }
                }, IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }
        }

        /**
         * 创建索引
         * 
         * @param action 具体的创建索引的过程
         * @param mode 索引的打开模式
         * @throws java.io.IOException IO错误
         */
        private void updateIndex(IndexExecutor action, IndexWriterConfig.OpenMode mode) {
            Preconditions.checkNotNull(action);
            Preconditions.checkNotNull(mode);
            indexUpdater.updateIndex(action, mode);
            // 更新索引
            if (action != null) {
                indexUpdateCallback.afterUpdate();
            }
        }

        /**
         * 索引更新后调用的方法
         * 
         * @author hang.gao
         * 
         */
        static interface IndexUpdateCallback {
            void afterUpdate();
        }
    }
}
