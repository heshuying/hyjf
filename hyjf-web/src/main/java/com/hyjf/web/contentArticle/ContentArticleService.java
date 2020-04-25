package com.hyjf.web.contentArticle;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentArticle;

public interface ContentArticleService {

	
	/**
	 * 根据主键ID获取Aricle
	 * 
	 * @param id
	 * @return
	 */
	public ContentArticle getNoticeInfo(Integer id);

	 /**
     * 获取网站公告
     * */
    public int getNoticeListCount(String noticeType);
    
    /**
     * 获取网站公告列表
     * */
    public List<ContentArticle> searchNoticeList(String noticeType, int offset, int limit);
	
}