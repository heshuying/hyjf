package com.hyjf.app.news.config;

import com.hyjf.app.BaseDefine;

public class AppNewsConfigDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = AppNewsConfigController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/news/config";

	/** 开关闭推送服务  */
	public static final String UPDATE_APPNEWS_CONFIG_ACTION= "/updateAppNewsConfig";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + UPDATE_APPNEWS_CONFIG_ACTION;        
	
}
