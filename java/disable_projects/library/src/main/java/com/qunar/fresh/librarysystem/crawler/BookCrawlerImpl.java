package com.qunar.fresh.librarysystem.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.qunar.fresh.librarysystem.io.ResourceAccess;

/**
 * 
 * @author jing.lv
 * 
 */
public class BookCrawlerImpl implements BookCrawler {

    /**
     * 用于加载URL与存储/打开资源
     */
    private ResourceAccess resourceAccess;

    /**
     * 搜索图书的基本URL
     */
    private String searchUrl;

    /**
     * 获取图书的具体信息的URL
     */
    private String bookBaseUrl;

    /**
     * 编码参数时的字符集
     */
    private String urlEncoding;

    /*
     * (non-Javadoc)
     * @see com.qunar.fresh.librarysystem.crawler.BookInfoExtractor#getHtml(java.lang.String)
     */
    @Override
    public String downloadHtml(String url) {
        return resourceAccess.loadText(url);
    }

    /*
     * (non-Javadoc)
     * @see com.qunar.fresh.librarysystem.crawler.BookInfoExtractor#parseToSearchURL(java.lang.String)
     */
    @Override
    public String parseToSearchURL(String bookName) {
        StringBuilder searchURL = new StringBuilder(searchUrl);
        String encodeBookNameURLEncoder;
        try {
            encodeBookNameURLEncoder = URLEncoder.encode(bookName, urlEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        searchURL.append(encodeBookNameURLEncoder).append("&cat=1001");
        return searchURL.toString();
    }

    /*
     * (non-Javadoc)
     * @see com.qunar.fresh.librarysystem.crawler.BookInfoExtractor#extractBookURL(java.lang.String)
     */
    @Override
    public String extractBookURL(String html) {
        String bookFlag = bookBaseUrl;
        int startIndex = html.indexOf(bookFlag);
        int endIndex = html.indexOf("/", startIndex + bookFlag.length()) + 1;
        return html.substring(startIndex, endIndex);
    }

    /*
     * (non-Javadoc)
     * @see com.qunar.fresh.librarysystem.crawler.BookInfoExtractor#downloadImage(java.lang.String)
     */
    @Override
    public String copyUrlToLocal(String imageURL) {
        return resourceAccess.loadAndStore(imageURL);
    }

    public void setResourceAccess(ResourceAccess resourceAccess) {
        this.resourceAccess = resourceAccess;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public void setBookBaseUrl(String bookBaseUrl) {
        this.bookBaseUrl = bookBaseUrl;
    }

    public void setUrlEncoding(String urlEncoding) {
        this.urlEncoding = urlEncoding;
    }

}