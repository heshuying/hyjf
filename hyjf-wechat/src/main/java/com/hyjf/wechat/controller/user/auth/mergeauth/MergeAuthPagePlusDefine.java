package com.hyjf.wechat.controller.user.auth.mergeauth;

import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.wechat.base.BaseDefine;


public class MergeAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = MergeAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/mergeauthpageplus";
	/** 合并授权 */
    public static final String MERGE_AUTH_ACTION = "/page";

    /** 用户授权成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/user/setting/mergeauth/result/success";
    /** 用户授权失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/user/setting/mergeauth/result/failed";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/mergeAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/mergeAuthBgreturn";
    
    public static final String AUTH_TYPE = AuthBean.AUTH_TYPE_MERGE_AUTH;
    
    
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/user/setting/quota/result/failed";

}
