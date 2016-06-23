package com.qunar.fresh.librarysystem.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.qunar.fresh.librarysystem.dao.CategoryDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-1 Time: 下午6:03 To change this template use File | Settings |
 * File Templates.
 */
@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    @Resource
    private CategoryDao categoryDao;

    @Resource
    private BookService bookService;

    public CategoryService(CategoryDao categoryDao, BookService bookService) {
        this.categoryDao = categoryDao;
        this.bookService = bookService;
    }

    public CategoryService() {

    }

    /**
     * 从数据库获得top的分类，第一个是默认的不限分类
     * 
     * @return
     */
    public List<Category> fetchTopKindList() {
        List<Category> categoryList = categoryDao.fetchTopKindList();
        List<Category> kindHotList = Lists.newArrayList();
        int hot;
        for (Category category : categoryList) {
            hot = bookService.fetchHotByNavId(category.getId());
            category.setHot(hot);
            kindHotList.add(category);
        }
        Collections.sort(kindHotList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return Ints.compare(o2.getHot(), o1.getHot());
            }
        });
        int kindLen = categoryList.size();
        int topKindNum = (kindLen < 5) ? kindLen : 5;

        logger.info("获得top分类的列表");
        return kindHotList.subList(0, topKindNum);
    }

    /**
     * 把从数据库得到的分类信息中选择最多5个top分类信息用来展示，第一个是默认的不限
     * 
     * @param topN
     * @return
     */
    public Map<String, Object> getLoadTopKindInfo(int topN) {
        Map<String, Object> loadTopKindInfoMap = Maps.newHashMap();
        int bookTypeId = 0;
        List<Book> bookList = fetchDefaultTopBooks(topN, bookTypeId);
        List<Category> topKindList = fetchTopKindList();
        topKindList.add(0, new Category(0, "不限", "不限", 0));
        List<Map<String, Object>> jsonTopKindList = Lists.transform(topKindList,
                new Function<Category, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(Category input) {
                        return input.toJsonKindMap();
                    }
                });
        int kindListLen = topKindList.size();
        loadTopKindInfoMap.put("total", kindListLen);
        loadTopKindInfoMap.put("typeList", jsonTopKindList);
        loadTopKindInfoMap.put("resultList", bookList);
        logger.info("获得完整top分类信息");
        return loadTopKindInfoMap;
    }

    /**
     * 获取第一个不限分类的top书
     * 
     * @param topN
     * @param bookTypeId
     * @return
     */
    public List<Book> fetchDefaultTopBooks(int topN, int bookTypeId) {
        logger.info("获得不限分类的top书");
        return bookService.top(topN, bookTypeId);
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public BookService getBookService() {
        return bookService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
