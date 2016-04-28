package com.x.demo.model;

import java.util.Date;

public class CategoryInfo {

    private Long id;
    private Long parentId;

    private String name;

    private Date createDate;
    private Date modifyDate;

    private Boolean visible;
    private Boolean deleted;

    public CategoryInfo() {
    }

    public CategoryInfo(Long parentId, String name) {
        this.id = null;
        this.parentId = parentId;
        this.name = name;

        this.createDate = new Date();
        this.modifyDate = this.createDate;

        this.visible = true;
        this.deleted = false;
    }

    public CategoryInfo(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;

        this.createDate = new Date();
        this.modifyDate = this.createDate;

        this.visible = true;
        this.deleted = false;
    }

    public CategoryInfo(Long id, Long parentId, String name, Date createDate, Date modifyDate, Boolean visible, Boolean deleted) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.visible = visible;
        this.deleted = deleted;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public CategoryInfo(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

