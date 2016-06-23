package com.qunar.fresh.librarysystem.controller;

import com.qunar.fresh.librarysystem.service.impl.CategoryService;
import com.qunar.fresh.librarysystem.service.impl.HomePageService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-8
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
public class HomePageControllerTest {
    private HomePageService homePageService;
    private CategoryService categoryService;
    private HomePageController homePageController;
    private HttpServletRequest request;

    @Before
    public void doSetup() {
        homePageService = mock(HomePageService.class);
        categoryService = mock(CategoryService.class);
        homePageController = new HomePageController();
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void testLoadHomePage_should_return_right_result() {
        when(homePageService.fetchAllCategoriesInfo()).thenReturn(new HashMap<String, Object>());
        when(categoryService.getLoadTopKindInfo(anyInt())).thenReturn(new HashMap<String, Object>());

        homePageController.loadHomePage(request);
        verify(homePageService).fetchAllCategoriesInfo();
        verify(categoryService).getLoadTopKindInfo(anyInt());
    }
    @Test
    public void testLoadHomePage_should_return_errcode2_when_topKindInfo_is_null(){
        when(homePageService.fetchAllCategoriesInfo()).thenReturn(new HashMap<String, Object>());
        when(categoryService.getLoadTopKindInfo(anyInt())).thenReturn(null);
        Map<String,Object> jsonResult =  homePageController.loadHomePage(request);
        assertEquals(2,jsonResult.get("status"));
    }
    @Test
    public void testLoadHomePage_should_return_errcode1_when_category_is_null(){
        when(homePageService.fetchAllCategoriesInfo()).thenReturn(null);
        when(categoryService.getLoadTopKindInfo(anyInt())).thenReturn(new HashMap<String, Object>());
        Map<String,Object> jsonResult =  homePageController.loadHomePage(request);
        assertEquals(1,jsonResult.get("status"));
    }
}
