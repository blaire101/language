package com.qunar.fresh.librarysystem.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jinglv
 * Date: 14-4-3
 * Time: 下午12:17
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:extractor/*")
public class BookCrawlerImplTest {
    @Resource
    BookCrawler webCrawler;

    @Test
    public void testGetHtml() throws IOException {
        String html = webCrawler.downloadHtml("http://www.baidu.com");
        Assert.assertTrue(html.contains("百度一下，你就知道"));
    }

    @Test
    public void testParseToSearchURL() throws UnsupportedEncodingException, MalformedURLException {
        String url = webCrawler.parseToSearchURL("C++Primer");
        Assert.assertTrue(url.equals("http://book.douban.com/subject_search?search_text=C%2B%2BPrimer&cat=1001"));
    }

    @Test
    public void testGetBookURL() throws IOException {
    	String url = webCrawler.parseToSearchURL("C++Primer");
        String html = webCrawler.downloadHtml(url);
        String bookUrl = webCrawler.extractBookURL(html);
        Assert.assertTrue(bookUrl.toString().equals("http://book.douban.com/subject/1767741/"));
    }

    @Test
    public void testDownloadImage() throws IOException {
        webCrawler.copyUrlToLocal("http://img3.douban.com/mpic/s1638975.jpg");
    }
}
