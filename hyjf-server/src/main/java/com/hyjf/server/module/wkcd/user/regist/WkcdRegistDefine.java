package com.hyjf.server.module.wkcd.user.regist;

import com.hyjf.server.BaseDefine;

public class WkcdRegistDefine extends BaseDefine {

	/** 类名 */
	public static final String THIS_CLASS = WkcdRegistController.class.getName();
	
	/** 预注册Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/user/regist";

	/** 用户注册 @RequestMapping值 */
	public static final String USER_REGIST_ACTION = "registAction";


}
