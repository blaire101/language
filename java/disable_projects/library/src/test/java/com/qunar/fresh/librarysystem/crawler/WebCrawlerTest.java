package com.qunar.fresh.librarysystem.crawler;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: jinglv
 * Date: 14-4-3
 * Time: 下午12:17
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:extractor/*")
public class WebCrawlerTest {
//    @Resource
//    WebCrawler webCrawler;
//
//    @Test
//    @Ignore
//    public void testGetHtml() throws IOException {
//        String html = webCrawler.getHtml(new URL("http://www.baidu.com"));
//        Assert.assertTrue(html.contains("百度一下，你就知道"));
//    }
//
//    @Test
//    @Ignore
//    public void testParseToSearchURL() throws UnsupportedEncodingException, MalformedURLException {
//        URL url = webCrawler.parseToSearchURL("C++Primer");
//        Assert.assertTrue(url.toString().equals("http://book.douban.com/subject_search?search_text=C%2B%2BPrimer&cat=1001"));
//    }
//
//    @Test
//    @Ignore
//    public void testGetBookURL() throws IOException {
//        URL url = webCrawler.parseToSearchURL("C++Primer");
//        String html = webCrawler.getHtml(url);
//        URL bookUrl = webCrawler.getBookURL(html);
//        Assert.assertTrue(bookUrl.toString().equals("http://book.douban.com/subject/1767741/"));
//    }
//
//    @Test
//    @Ignore
//    public void testDownloadImage() throws IOException {
//        webCrawler.downloadImage("http://img3.douban.com/mpic/s1638975.jpg");
//    }
}
