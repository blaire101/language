package com.qunar.fresh.librarysystem.search.book;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.base.Optional;

import com.qunar.fresh.librarysystem.dao.Page;
import com.qunar.fresh.librarysystem.utils.ChineseToPinyin;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.search.AnalyzerFactory;
import com.qunar.fresh.librarysystem.search.Closeableiterable;
import com.qunar.fresh.librarysystem.search.IndexUpdateTask;
import com.qunar.fresh.librarysystem.search.LuceneExecutor;
import com.qunar.fresh.librarysystem.search.SearchExecutor;
import com.qunar.fresh.librarysystem.search.SearchResult;
import com.qunar.fresh.librarysystem.search.db.DatabaseIter;
import com.qunar.fresh.librarysystem.search.index.IndexUpdater;

/**
 * 搜索数据库中的内容，为数据库创建全文索引
 * <p/>
 * 
 * @author hang.gao Initial Created at 2014年3月27日
 *         <p/>
 */
public class DatabaseBookSearchEngine implements BookSearchEngine {

    /**
     * 索引的目录
     */
    private String indexDirectory;

    /**
     * 索引的目录
     */
    private Directory directory;

    /**
     * 是否重新创建索引，如果为true，在系统启动时会索引数据库
     */
    private boolean recreateIndexOnStart = false;

    /**
     * 执行搜索与索引的创建
     */
    private SearchExecutor searchExecutor;

    /**
     * 队列的长度
     */
    private int queueSize = 5;

    /**
     * 搜索索引中的域
     */
    private String[] searchFields = { IndexField.AUTHOR.toString(), IndexField.AUTHOR_PINYIN.toString(), IndexField.BOOK_NAME.toString(), IndexField.BOOK_NAME_PINYIN.toString(),
            IndexField.BOOK_TYPE.toString(), IndexField.BOOK_TYPE_PINYIN.toString(), IndexField.BOOK_INTRO.toString(), IndexField.BOOK_INTRO_PINYIN.toString() };

    /**
     * 从索引中返回的域
     */
    private String[] docFields = { IndexField.BOOK_NAME.toString() };

    private String[] bookNameField = { IndexField.BOOK_NAME.toString() };

    /**
     * 分词器工厂
     */
    private AnalyzerFactory analyzerFactory;

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(DatabaseBookSearchEngine.class);

    /**
     * 初始数据访问
     */
    private DatabaseIter databaseIter;

    public void init() {
        logger.debug("Index directory is {}.", indexDirectory);
        logger.debug("Initial the search engine.");
        File file = new File(indexDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            directory = new NIOFSDirectory(file);
            if (searchExecutor == null) {
                searchExecutor = getSearchExecutor();
            }
            if (recreateIndexOnStart) {
                if (databaseIter == null) {
                    databaseIter = getDefaultDatabaseIter();
                }
                searchExecutor.init(new InitialDatabaseIndexCreator());
            } else {
                searchExecutor.init();
            }
        } catch (IOException e) {
            throw new ResourceAccessException("Access the direct failed", e);
        }
        logger.debug("Initial search engine success.");
    }

    protected DatabaseIter getDefaultDatabaseIter() {
        return new com.qunar.fresh.librarysystem.search.db.DataBaseIterImpl();
    }

    /**
     * @return
     */
    protected SearchExecutor getSearchExecutor() {
        return new LuceneExecutor(directory, queueSize, analyzerFactory, null);
    }

    @Override
    public SearchResult searchBook(String keywords, int start, int resultCount) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkArgument(start >= 0 && resultCount >= 1);
        return Optional.fromNullable(searchExecutor.search(keywords, start, resultCount, searchFields, docFields)).or(
                SearchResult.NONE);
    }

    @Override
    public SearchResult searchBookByName(String keywords, Page page) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keywords));
        Preconditions.checkNotNull(page);
        return Optional.fromNullable(
                searchExecutor.search(keywords, page.getOffset(), page.getLimit(), bookNameField, docFields)).or(
                SearchResult.NONE);
    }

    @Override
    public void apendToIndex(Book book) {
        searchExecutor.updateIndex(new BookIndexAppendTask(book));
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public void setRecreateIndexOnStart(boolean recreateIndexOnStart) {
        this.recreateIndexOnStart = recreateIndexOnStart;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public void setSearchFields(String[] searchFields) {
        this.searchFields = searchFields;
    }

    public void setDocFields(String[] docFields) {
        this.docFields = docFields;
    }

    public AnalyzerFactory getAnalyzerFactory() {
        return analyzerFactory;
    }

    public void setAnalyzerFactory(AnalyzerFactory analyzerFactory) {
        this.analyzerFactory = analyzerFactory;
    }

    @Override
    public void updateIndex(Book book) {
        searchExecutor.updateIndex(new BookIndexModifyTask(book));
    }

    @Override
    public void deleteIndex(int bookInfoId) {
        searchExecutor.updateIndex(new BookIndexDeleteTask(bookInfoId));
    }

    public void setDatabaseIter(DatabaseIter databaseIter) {
        this.databaseIter = databaseIter;
    }

    public void setSearchExecutor(SearchExecutor searchExecutor) {
        this.searchExecutor = searchExecutor;
    }

    public void destroy() throws Exception {
        searchExecutor.destroy();
    }

    private final class PinyinTransform implements Function<String, String> {
		public String apply(String input) {
			return ChineseToPinyin.transform(input);
		}
	}

	/**
     * 系统初始时，将数据库中的已有数据创建索引
     * 
     * @author hang.gao
     */
    final class InitialDatabaseIndexCreator implements IndexUpdater.IndexExecutor {

        /**
         * 创建索引时执行的查询
         */
        private final String selectSql = "select book_info.id, book_info.book_name, book_info.author, navigation.book_type, book_info.press, book_info.intro from book_info, navigation where book_info.nav_id = navigation.id";
        // private final String selectSql =
        // "select book.book_id, book_info.book_name, book_info.author, book.lib_id, navigation.book_type, book_info.press, book_info.intro, library.lib_name, library.lib_dept from book_info, book, navigation, library where book.lib_id = library.id and book_info.nav_id = navigation.id and book_info.id = book.book_info_id group by book.lib_id, book_info.book_name";

        /**
         * 书名在数据库中的列名
         */
        private static final String BOOK_NAME_COLUMN = "book_name";

        /**
         * 书的作者字段
         */
        private static final String BOOK_AUTHOR = "author";

        /**
         * 书的类型
         */
        private static final String BOOK_TYPE = "book_type";

        /**
         * 图书的简介
         */
        private static final String BOOK_INTRO = "intro";

        /**
         * 图书信息的id
         */
        private static final String BOOK_INFO_ID = "id";

        public void onUpdate(IndexWriter indexWriter) {
            Preconditions.checkNotNull(indexWriter);
            Closeableiterable<Book> iter = null;
            try {
                iter = databaseIter.select(selectSql, new RowMapper<Book>() {

                    @Override
                    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Book book = new Book();
                        book.setBookName(rs.getString(BOOK_NAME_COLUMN));
                        book.setBookAuthor(rs.getString(BOOK_AUTHOR));
                        book.setBookType(rs.getString(BOOK_TYPE));
                        book.setBookIntro(rs.getString(BOOK_INTRO));
                        book.setBookInfoId(rs.getInt(BOOK_INFO_ID));
                        return book;
                    }
                });
                for (Book book : iter) {
                    indexWriter.addDocument(createDocument(book));
                }
            } catch (IOException e) {
                throw new ResourceAccessException("Add document error", e);
            } finally {
                if (iter != null) {
                    iter.close();
                }
            }
        }

    }

    
    /**
     * 连接拼音时使用
     */
    private final Joiner joiner = Joiner.on(' ');

	private PinyinTransform pinyinTransform = new PinyinTransform();
    /**
     * 创建文档
     * 
     * @param book 需要创建索引的书
     * @return 需要创建索引的书对应的Document
     * @throws IOException 
     */
    private Document createDocument(Book book) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(IndexField.BOOK_NAME.toString(), book.getBookName(), Store.YES));
        doc.add(new TextField(IndexField.BOOK_NAME_PINYIN.toString(), joiner.join(Lists.transform(searchExecutor.getQueryParser().tokenString(book.getBookName(), IndexField.BOOK_NAME_PINYIN.toString()), pinyinTransform )), Store.YES));
        doc.add(new TextField(IndexField.AUTHOR.toString(), book.getBookAuthor(), Store.YES));
        doc.add(new TextField(IndexField.AUTHOR_PINYIN.toString(), joiner.join(Lists.transform(searchExecutor.getQueryParser().tokenString(book.getBookAuthor(), IndexField.AUTHOR.toString()), pinyinTransform)), Store.YES));
        doc.add(new TextField(IndexField.BOOK_TYPE.toString(), book.getBookType(), Store.YES));
        doc.add(new TextField(IndexField.BOOK_TYPE_PINYIN.toString(), joiner.join(Lists.transform(searchExecutor.getQueryParser().tokenString(book.getBookType(), IndexField.BOOK_TYPE_PINYIN.toString()), pinyinTransform)), Store.YES));
//        doc.add(new TextField(IndexField.BOOK_INTRO_PINYIN.toString(), ChineseToPinyin.transform(book.getBookIntro()), Store.YES));
        doc.add(new TextField(IndexField.BOOK_INTRO.toString(), book.getBookIntro(), Store.YES));
        doc.add(new TextField(IndexField.BOOK_INFO_ID.toString(), Integer.toString(book.getBookInfoId()), Store.YES));
        return doc;
    }

    /**
     * 
     * @author hang.gao
     * 
     */
    private abstract class AbstractIndexUpdateTask implements IndexUpdateTask {

        /**
         * 写入索引的对象
         */
        private IndexWriter indexWriter;

        public AbstractIndexUpdateTask() {
            super();
        }

        @Override
        public void setIndexWriter(IndexWriter indexWriter) {
            Preconditions.checkNotNull(indexWriter);
            this.indexWriter = indexWriter;
        }

        protected IndexWriter getIndexWriter() {
            return indexWriter;
        }

    }

    /**
     * 给新的图书添加索引的任务
     * 
     * @author hang.gao
     * 
     */
    private class BookIndexAppendTask extends AbstractIndexUpdateTask {

        /**
         * 需要新建索引的图书
         */
        private final Book book;

        public BookIndexAppendTask(Book book) {
            Preconditions.checkNotNull(book);
            this.book = book;
        }

        @Override
        public void run() throws IOException {
            getIndexWriter().addDocument(createDocument(book));
        }
    }

    /**
     * 删除索引的任务，只能按照书的book_id进行删除，因为它是唯一的
     * 
     * @author hang.gao
     * 
     */
    private class BookIndexDeleteTask extends AbstractIndexUpdateTask {

        /**
         * 书的条码
         */
        private final int bookInfoId;

        public BookIndexDeleteTask(int bookInfoId) {
            this.bookInfoId = bookInfoId;
        }

        @Override
        public void run() throws IOException {
            getIndexWriter()
                    .deleteDocuments(new Term(IndexField.BOOK_INFO_ID.toString(), Integer.toString(bookInfoId)));
        }

    }

    /**
     * 修改索引的任务
     * 
     * @author hang.gao
     * 
     */
    private class BookIndexModifyTask extends BookIndexAppendTask {

        /**
         * 需要新建索引的图书
         */
        private final Book book;

        public BookIndexModifyTask(Book book) {
            super(book);
            Preconditions.checkNotNull(book);
            this.book = book;
        }

        @Override
        public void run() throws IOException {
            getIndexWriter().deleteDocuments(
                    new Term(IndexField.BOOK_INFO_ID.toString(), Integer.toString(book.getBookInfoId())));
            super.run();
        }

    }

}
