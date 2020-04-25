package com.hyjf.app.sharenews;

import com.hyjf.app.BaseDefine;

public class ShareNewsDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = ShareNewsController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/shareNews";

	/** 获取分享信息  */
	public static final String GETNEWS_ACTION = "/getNews";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + GETNEWS_ACTION;
	
}
