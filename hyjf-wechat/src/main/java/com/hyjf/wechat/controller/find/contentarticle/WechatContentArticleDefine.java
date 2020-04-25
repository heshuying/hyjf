package com.hyjf.wechat.controller.find.contentarticle;

import com.hyjf.wechat.base.BaseDefine;

public class WechatContentArticleDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = WechatContentArticleController.class.getName();
	
	public static final String KNOWLEDGE_TYPE="3";

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/find/contentArticle";

	/** 根据文章类型返回文章列表  */
	public static final String GET_CONTENT_ARTICLE_LIST_BY_TYPE_ACTION = "/getContentArticleListByType";
	      
	/** 获取文章详情  */
    public static final String GET_CONTENT_ARTICLE_ID_ACTION = "/{type}/{contentArticleId}";
    /** 获取文章详情翻页信息  */
    public static final String GET_CONTENT_ARTICLE_FLIP_ACTION = "/getContentArticleFlip";
    
}
