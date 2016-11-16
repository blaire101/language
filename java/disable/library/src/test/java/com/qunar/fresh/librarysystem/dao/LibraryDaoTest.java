package com.qunar.fresh.librarysystem.dao;

import javax.annotation.Resource;

import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 
 * @author hang.gao
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dao.xml")
public class LibraryDaoTest {

	@Resource
	private LibraryDao libraryDao;
	
	@Test
	public void testSelectBorrowPeriod() {
		Assert.assertEquals(15, libraryDao.selectRedecoratePeriod(1).intValue());
	}

    @Test
    public void test_addOneLibrary(){
        boolean addSuccess = libraryDao.addOneLibrary("应届生","学生图书馆",LibraryStatus.VALID);
        Library library = libraryDao.fetchLibInfo(null,"酒店事业部","酒店图书馆", LibraryStatus.VALID);

        assertTrue(addSuccess);
    }

    @Test
    public void test_fetchLibInfo(){
        Library library = libraryDao.fetchLibInfo(null,"酒店事业部","酒店图书馆", LibraryStatus.VALID);
        Assert.assertNotNull(library);
    }

    @Test
    public void test_libCount(){
        int count = libraryDao.libCount("应届生","学生图书馆",LibraryStatus.VALID);
        assertEquals(1,count);
    }

    @Test
    public void test_deleteLibrary(){
        libraryDao.deleteLibrary(3);
        Library library = libraryDao.fetchLibInfo(null,"应届生","学生图书馆", LibraryStatus.VALID);
        assertNull(library);
    }

}
