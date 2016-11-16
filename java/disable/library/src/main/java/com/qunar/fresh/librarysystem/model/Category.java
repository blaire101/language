package com.qunar.fresh.librarysystem.model;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 图书的分类 //--------------------- Change Logs---------------------- //
 * <p/>
 * 
 * @author hang.gao Initial Created at 2014年3月28日
 *         <p/>
 *         //-------------------------------------------------------
 */
public class Category {

    /**
     * 自增主键
     */
    private int id;

    /**
     * 父类，总的分类
     */
    private String title;

    /**
     * 书的分类
     */
    private String bookType;

    /**
     * 图书馆编号
     */
    private int libId;

    private int hot;

    public Category() {
    }

    public Category(int id, String title, String bookType, int libId) {
        this.id = id;
        this.title = title;
        this.bookType = bookType;
        this.libId = libId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public Map<String, Object> toJsonNavMap() {
        Map<String, Object> categoryMap = Maps.newHashMap();
        categoryMap.put("navId", getId());
        categoryMap.put("title", getTitle());
        categoryMap.put("bookType", getBookType());
        categoryMap.put("libId", getLibId());
        return categoryMap;
    }

    public Map<String, Object> toJsonKindMap() {
        Map<String, Object> categoryMap = Maps.newHashMap();
        categoryMap.put("kindType", getId());
        categoryMap.put("title", getTitle());
        categoryMap.put("bookType", getBookType());
        categoryMap.put("libId", getLibId());
        return categoryMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append("/").append(getTitle()).append("/").append(getBookType()).append("/")
                .append(getLibId());
        return sb.toString();
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }
}
