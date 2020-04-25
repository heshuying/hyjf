package com.hyjf.web.contentArticle;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.web.WebBaseAjaxResultBean;


public class ContentArticleResult extends WebBaseAjaxResultBean {
	/**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9024738192216239761L;

    private List<ContentArticle> contentArticleList;

    public List<ContentArticle> getContentArticleList() {
        return contentArticleList;
    }

    public void setContentArticleList(List<ContentArticle> contentArticleList) {
        this.contentArticleList = contentArticleList;
    }

	

}