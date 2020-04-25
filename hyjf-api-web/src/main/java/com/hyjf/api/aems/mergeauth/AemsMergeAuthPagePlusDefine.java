package com.hyjf.api.aems.mergeauth;

import com.hyjf.base.bean.BaseDefine;

public class AemsMergeAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = AemsMergeAuthPagePlusServer.class.getName();

	/** 外部服务接口:多合一授权 @RequestMapping */
	public static final String REQUEST_MAPPING = "/aems/mergeauth";
	/** 多合一授权 @RequestMapping */
	public static final String MERGE_AUTH_ACTION = "/mergeAuth";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/mergeauthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/mergeauthBgreturn";
	
	/**错误页面*/
    public static final String PATH_AUTH_PAGE_ERROR = "/bank/user/trusteePay/error";
    
    

}
