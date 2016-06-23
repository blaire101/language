package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-1 Time: 下午5:38 To change this template use File | Settings |
 * File Templates.
 */
@Repository
public interface CategoryDao {
    public List<Category> fetchTopKindList();
}
