package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class AppBorrowImage implements Serializable {
    private Integer id;

    private String borrowImage;

    private String borrowImageTitle;

    private String borrowImageName;

    private String borrowImageRealname;

    private String borrowImageUrl;

    private String notes;

    private Integer addtime;

    private Integer updatetime;

    private Integer sort;

    private String pageUrl;

    private String pageType;

    private String version;

    private Integer status;

    private String versionMax;

    private String jumpName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBorrowImage() {
        return borrowImage;
    }

    public void setBorrowImage(String borrowImage) {
        this.borrowImage = borrowImage == null ? null : borrowImage.trim();
    }

    public String getBorrowImageTitle() {
        return borrowImageTitle;
    }

    public void setBorrowImageTitle(String borrowImageTitle) {
        this.borrowImageTitle = borrowImageTitle == null ? null : borrowImageTitle.trim();
    }

    public String getBorrowImageName() {
        return borrowImageName;
    }

    public void setBorrowImageName(String borrowImageName) {
        this.borrowImageName = borrowImageName == null ? null : borrowImageName.trim();
    }

    public String getBorrowImageRealname() {
        return borrowImageRealname;
    }

    public void setBorrowImageRealname(String borrowImageRealname) {
        this.borrowImageRealname = borrowImageRealname == null ? null : borrowImageRealname.trim();
    }

    public String getBorrowImageUrl() {
        return borrowImageUrl;
    }

    public void setBorrowImageUrl(String borrowImageUrl) {
        this.borrowImageUrl = borrowImageUrl == null ? null : borrowImageUrl.trim();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }

    public Integer getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl == null ? null : pageUrl.trim();
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType == null ? null : pageType.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVersionMax() {
        return versionMax;
    }

    public void setVersionMax(String versionMax) {
        this.versionMax = versionMax == null ? null : versionMax.trim();
    }

    public String getJumpName() {
        return jumpName;
    }

    public void setJumpName(String jumpName) {
        this.jumpName = jumpName == null ? null : jumpName.trim();
    }
}