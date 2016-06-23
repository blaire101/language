package com.qunar.fresh.librarysystem.crawler;

import com.qunar.fresh.librarysystem.model.Book;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jinglv
 * Date: 14-4-3
 * Time: 下午2:34
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:extractor/*.xml")
public class BookExtractorTest {
    @Resource
    BookExtractor bookExtractor;

    @Test
    @Ignore
    public void testGetBookInfoFromDouban() throws IOException {
        Book bookInfo = bookExtractor.generate("C++Primer");
        Assert.assertTrue(bookInfo.getBookPress().equals("人民邮电出版社"));
    }
}
