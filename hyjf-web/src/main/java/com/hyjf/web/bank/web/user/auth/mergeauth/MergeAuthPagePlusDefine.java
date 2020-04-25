package com.hyjf.web.bank.web.user.auth.mergeauth;

import com.hyjf.web.BaseDefine;

public class MergeAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = MergeAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/mergeauthpageplus";
	/** 合并授权 */
    public static final String MERGE_AUTH_ACTION = "/page";

    /** 合并授权成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/autoplus/auto-tender-success";
    /** 合并授权失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/autoplus/auto-tender-fail";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/mergeAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/mergeAuthBgreturn";
    
    
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/bank/user/autoplus/auto-tender-again";
}
