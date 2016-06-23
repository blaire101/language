package com.qunar.fresh.librarysystem.crawler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qunar.fresh.librarysystem.model.Book;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author jing.lv
 */
@Component
public class BookHtmlExtractor implements BookExtractor {

    private static final int INTRO_LENGTH = 512;

    @Resource
    private BookCrawler webCrawler;

    /**
     * 根据书名从豆瓣上得到该书的出版社、图片链接、简介信息
     * 
     * @see com.qunar.fresh.librarysystem.crawler.BookExtractor#(java.lang.String)
     */
    @Override
    public Book generate(String bookName) {
        String bookListHtml = webCrawler.downloadHtml(webCrawler.parseToSearchURL(bookName));
        String bookInfoHtml = webCrawler.downloadHtml(webCrawler.extractBookURL(bookListHtml));
        return getBookInfo(bookInfoHtml);
    }

    /* 根据页面html获得书籍信息:出版社、图片链接、简介 */
    private Book getBookInfo(String html) {
        checkArgument(html != null && !"".equals(html));
        Book book = new Book();
        String press = "***";
        String pressFlag = "出版社:</span>";
        int pressStart = html.indexOf(pressFlag);
        if (pressStart != -1) {
            int pressEnd = html.indexOf("<", pressStart + pressFlag.length());
            press = html.substring(pressStart + pressFlag.length(), pressEnd).trim();
        }
        book.setBookPress(press);
        String imageURLFlag = "<img src=";
        int imageStartIndex = html.indexOf(imageURLFlag);
        int imageEndIndex = html.indexOf("title", imageStartIndex);
        String imageURL = html.substring(imageStartIndex + imageURLFlag.length() + 1, imageEndIndex - 2);
        String localImageURL = webCrawler.copyUrlToLocal(imageURL);
        book.setImageURL(localImageURL);
        String introLetter = "intro";
        int fromIndex = html.indexOf(introLetter);
        if (fromIndex != -1) {
            String introFlag = "<p>";
            int introStartIndex = html.indexOf(introFlag, fromIndex);
            int introEndIndex = html.indexOf("</p>", introStartIndex);
            String intro = html.substring(introStartIndex + introFlag.length(), introEndIndex).trim();
            if (intro.length() > INTRO_LENGTH) {
                intro = intro.substring(0, INTRO_LENGTH);
            }
            book.setBookIntro(intro);
        } else {
            book.setBookIntro("暂无书籍简介");
        }
        return book;
    }
}