package com.hyjf.mybatis.model.customize;

import java.util.Date;

import com.hyjf.mybatis.model.auto.ContentArticle;

public class ContentArticleCustomize extends ContentArticle {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // 传参创建时间范围
    protected Date startCreate;

    protected Date endCreate;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public Date getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(Date startCreate) {
        this.startCreate = startCreate;
    }

    public Date getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(Date endCreate) {
        this.endCreate = endCreate;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

}
