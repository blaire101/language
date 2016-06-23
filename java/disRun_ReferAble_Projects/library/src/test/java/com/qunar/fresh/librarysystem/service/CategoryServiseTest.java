package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.CategoryDao;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.service.impl.BookServiceImpl;
import com.qunar.fresh.librarysystem.service.impl.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-4
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class CategoryServiseTest {

    private CategoryService categoryService;
    private CategoryDao  categoryDao;
    private BookServiceImpl bookService;


    @Before
    public void doSetUp() {
        categoryDao = mock(CategoryDao.class);
        bookService = mock(BookServiceImpl.class);
        categoryService = new CategoryService(categoryDao,bookService);

    }

    @Test
    public void testFetchDefaultTopBooks() {
        when(bookService.top(anyInt(), anyInt())).thenReturn(new ArrayList<Book>());
        categoryService.fetchDefaultTopBooks(anyInt(), anyInt());
        verify(bookService).top(anyInt(), anyInt());
    }

    @Test
    public void testFetchTopKindList_should_return_sizeOf4_with_4sizeAllTopKinds(){
        List<Category> topAllKindList = Lists.newArrayList();
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());
        when(categoryDao.fetchTopKindList()).thenReturn(topAllKindList);
        List<Category> topKindList = categoryService.fetchTopKindList();
        verify(categoryDao).fetchTopKindList();
        assertEquals(4,topKindList.size());
    }
    @Test
    public void testFetchTopKindList_should_return_sizeOf5_with_sizeLargerThan5AllTopKinds(){
        List<Category> topAllKindList = Lists.newArrayList();
        topAllKindList.add(new Category());    //1
        topAllKindList.add(new Category());    //2
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());
        topAllKindList.add(new Category());    //6
        when(categoryDao.fetchTopKindList()).thenReturn(topAllKindList);
        List<Category> topKindList = categoryService.fetchTopKindList();
        verify(categoryDao).fetchTopKindList();
        assertEquals(5,topKindList.size());
    }

    @Test
    public void testGetLoadTopKindInfo(){
        when(categoryService.fetchDefaultTopBooks(10,0)).thenReturn(new ArrayList<Book>());
        when(categoryService.fetchTopKindList()).thenReturn(new ArrayList<Category>());
        Map<String,Object> result = categoryService.getLoadTopKindInfo(10);
        assertTrue(result.containsKey("kindType"));
    }

    @Test
    public void test_fetchTopKindList(){
        List<Category> topkindList = categoryService.fetchTopKindList();
    }

}
