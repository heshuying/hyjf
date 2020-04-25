package com.hyjf.app.user.myasset;

import com.hyjf.app.BaseDefine;

public class MyAssetDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = MyAssetController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/myasset";

	/** 获取我的财富  */
	public static final String MYASSET_ACTION = "/info";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + MYASSET_ACTION;
	
}
