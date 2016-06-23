package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.dao.HomePageDao;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.service.impl.HomePageService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-7
 * Time: 下午8:35
 * To change this template use File | Settings | File Templates.
 */
public class HomePageServiceTest {

    private HomePageDao homePageDao;
    private HomePageService homePageService;

    @Before
    public void doSetUp() {
        homePageDao = mock(HomePageDao.class);
        homePageService = new HomePageService();
        homePageService.setHomePageDao(homePageDao);
    }

    @Test
    public void testFetchAllCategoriesInfo() {
        when(homePageDao.getCategories(null)).thenReturn(new ArrayList<Category>());
        homePageService.fetchAllCategoriesInfo();
        verify(homePageDao).getCategories(null);
    }
}
