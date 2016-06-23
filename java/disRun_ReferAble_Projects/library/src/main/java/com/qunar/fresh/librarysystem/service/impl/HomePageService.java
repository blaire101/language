package com.qunar.fresh.librarysystem.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.qunar.fresh.librarysystem.dao.HomePageDao;
import com.qunar.fresh.librarysystem.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-1 Time: 上午11:14 To change this template use File | Settings |
 * File Templates.
 */
@Service
public class HomePageService {
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    @Resource
    private HomePageDao homePageDao;

    /**
     * 获取读者首页所有导航分类和top分类信息
     * 
     * @return
     */
    public Map<String, Object> fetchAllCategoriesInfo() {
        List<Category> categoryList = homePageDao.getCategories(null);
        List<Map<String, Object>> titleCategoryMap = list2Map(categoryList);
        Map<String, Object> categoryMap = Maps.newHashMap();
        categoryMap.put("navList", titleCategoryMap);
        categoryMap.put("total", titleCategoryMap.size());
        logger.info("获得所有导航分类信息");
        return categoryMap;
    }

    /**
     * 把第一种格式转换成第二种格式
     * 
     * 第一种格式 nav: - [ - { id: 1, title: "计算机", bookType: "编程语言", libId: 1 }, - { id: 2, title: "计算机", bookType: "数据库",
     * libId: 1 }, - { id: 3, title: "人文", bookType: "闯关东", libId: 2 } ] 第二种格式 ： "navList":[ { "title":"aaa", //大分类标题
     * "list":[ {id:1, bookType:"sss" },{} ] //分类具体类别名称列表
     * 
     * },{ "title":"bbb", "list":[] } ]
     * 
     * @param categoryList
     * @return
     */
    private List<Map<String, Object>> list2Map(List<Category> categoryList) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Multimap<String, Category> titleCategoryMap = HashMultimap.create();
        for (Category category : categoryList) {
            String title = category.getTitle();
            titleCategoryMap.put(title, category);
        }
        Set<String> keySet = titleCategoryMap.keySet();
        for (String title : keySet) {
            Map<String, Object> titleMap = Maps.newHashMap();
            titleMap.put("title", title);
            Collection<Map<String, Object>> sameTitleCategorys = Collections2.transform(titleCategoryMap.get(title),
                    new Function<Category, Map<String, Object>>() {
                        @Override
                        public Map<String, Object> apply(Category category) {
                            return category.toJsonNavMap();
                        }
                    });
            titleMap.put("bookTypeList", sameTitleCategorys);
            mapList.add(titleMap);
        }
        logger.info("把分类列表转换成导航结构");
        return mapList;
    }

    public HomePageDao getHomePageDao() {
        return homePageDao;
    }

    public void setHomePageDao(HomePageDao homePageDao) {
        this.homePageDao = homePageDao;
    }
}
