package com.x.demo.service;

import com.x.demo.dao.CategoryDAO;
import com.x.demo.model.CategoryInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryInfoService {

    @Resource
    private CategoryDAO categoryDAO;

    public void addCategoryInfo(CategoryInfo categoryInfo) {

        this.categoryDAO.addCategoryInfo(categoryInfo);
    }

    public void addCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        try {
            this.categoryDAO.addCategoryInfoList(categoryInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}