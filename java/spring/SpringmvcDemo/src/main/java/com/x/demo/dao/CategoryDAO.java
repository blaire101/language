package com.x.demo.dao;

import com.x.demo.model.CategoryInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDAO {
    void addCategoryInfo(CategoryInfo categoryInfo);
    void addCategoryInfoList(List<CategoryInfo> categoryInfos);
}

