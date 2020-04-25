package com.hyjf.api.server.user.auth.mergeauth;

import com.hyjf.base.bean.BaseDefine;

public class MergeAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = MergeAuthPagePlusServer.class.getName();

    public static final String CONTROLLER_NAME = "ApiWebMergeAuthPagePlusServer";
	/** 外部服务接口:多合一授权 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/mergeAuthPagePlus";
	/** 多合一授权 @RequestMapping */
	public static final String MERGE_AUTH_ACTION = "/page";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/mergeauthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/mergeauthBgreturn";
	
	/**错误页面*/
    public static final String PATH_AUTH_PAGE_ERROR = "/bank/user/trusteePay/error";
    
    

}
