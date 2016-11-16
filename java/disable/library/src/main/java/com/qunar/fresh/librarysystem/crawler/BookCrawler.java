package com.qunar.fresh.librarysystem.crawler;

/**
 * 抽取图书信息
 * 
 * @author hang.gao
 * 
 */
public interface BookCrawler {

    /**
     * 下载页面
     * 
     * @param url 页面的URL
     * @return 页面中的内容
     */
    String downloadHtml(String url);

    /**
     * 将图书名转换成从Web上搜索图书的URL
     * 
     * @param bookName 图书名
     * @return 搜索图书的URL
     */
    String parseToSearchURL(String bookName);

    /**
     * 从HTML文本的内容中提取图书详细信息的URL
     * 
     * @param html 文本，从此html中提取图书的URL
     * @return 图书详细信息的URL
     */
    String extractBookURL(String html);

    /**
     * 将URL表示的资源复制到本地
     * 
     * @param url 资源的URL
     * @return 复制完数据后，返回提供外界访问此资源的URL
     */
    String copyUrlToLocal(String url);

}