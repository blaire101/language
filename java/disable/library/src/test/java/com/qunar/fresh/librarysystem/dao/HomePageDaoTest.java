package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: he.chen
 * Date: 14-4-16
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class HomePageDaoTest {

    @Resource
    HomePageDao homePageDao;

    @Test
    public void test_getCategories(){
        List<Category> categoryList = homePageDao.getCategories(null);
        assertNotNull(categoryList);
    }
}
