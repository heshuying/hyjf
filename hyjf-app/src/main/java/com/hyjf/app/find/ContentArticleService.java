package com.hyjf.app.find;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentArticle;

/**
 * 从web 拷贝的代码，没有做任何修改
 * 此处为类说明
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月8日
 * @see 下午5:46:14
 */
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