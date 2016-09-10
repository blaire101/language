package com.qunar.fresh.librarysystem.model;

import java.util.Map;

import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan libin.chen Date: 14-3-27 Time: 下午6:00 To
 * 
 * @author hang.gao
 * @author libin.chen
 * @author feiyan.shan
 */
public class Book {
    private int id;

    private int bookInfoId;

    private String bookId;

    private String bookName;

    private String bookAuthor;

    // 借出 未借 删除
    private BookStatus bookStatus;

    // 出版社
    private String bookPress;

    // 简介
    private String bookIntro;

    // 热度
    private int bookHot;

    // 大分类
    private String title;

    // 小分类
    private String bookType;

    // 所属图书馆id
    private int bookLib;

    private String libName;

    // 表示分类
    private int navId;

    private String imageUrl;

    /**
     * 可预约的数量
     */
    private int reserveCount;

    public Book() {
        this.id = 0;
        this.bookInfoId = 0;
        this.bookId = "";
        this.bookName = "";
        this.bookAuthor = "";
        this.bookStatus = BookStatus.DELETED;
        this.bookPress = "";
        this.bookIntro = "";
        this.bookHot = 0;
        this.title = "";
        this.bookType = "";
        this.bookLib = 0;
        this.libName = "";
        this.navId = 0;
    }

    /**
     * 书的数量
     */
    private int bookCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookInfoId() {
        return bookInfoId;
    }

    public void setBookInfoID(int bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getBookIntro() {
        return bookIntro;
    }

    public void setBookIntro(String bookIntro) {
        this.bookIntro = bookIntro;
    }

    public int getBookHot() {
        return bookHot;
    }

    public void setBookHot(int bookHot) {
        this.bookHot = bookHot;
    }

    public void setBookInfoId(int bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNavId(int navId) {
        this.navId = navId;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public int getBookLib() {
        return bookLib;
    }

    public void setBookLib(int bookLib) {
        this.bookLib = bookLib;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageURL(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNavId() {
        return navId;
    }

    public void setNavID(int navId) {
        this.navId = navId;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public int getReserveCount() {
        return reserveCount;
    }

    public void setReserveCount(int reserveCount) {
        this.reserveCount = reserveCount;
    }

    /**
     * 将对象里的数据转换成JSON时调用此方法，此方法的返回结果会被转换成JSON
     * 
     * @return 需要转换成JSON的数据的Map
     */
    public Map<String, Object> toJsonMap() {
        Map<String, Object> bookmap = Maps.newHashMap();
        bookmap.put("bookInfoId", getBookInfoId());
        bookmap.put("bookLib", getBookLib());
        bookmap.put("bookName", getBookName());
        bookmap.put("bookAuthor", getBookAuthor());
        bookmap.put("bookPress", getBookPress());
        bookmap.put("bookIntro", getBookIntro());
        bookmap.put("bookType", getBookType());
        bookmap.put("imageUrl", getImageUrl());
        bookmap.put("libName", getLibName());
        bookmap.put("reserveCount", reserveCount);
        return bookmap;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", bookInfoId=" + bookInfoId + ", bookId=" + bookId + ", bookName=" + bookName
                + ", bookAuthor=" + bookAuthor + ", bookStatus=" + bookStatus + ", bookPress=" + bookPress
                + ", bookIntro=" + bookIntro + ", bookHot=" + bookHot + ", title=" + title + ", bookType=" + bookType
                + ", bookLib=" + bookLib + ", libName=" + libName + ", navId=" + navId + ", imageUrl=" + imageUrl
                + ", reserveCount=" + reserveCount + ", bookCount=" + bookCount + "]";
    }

}
