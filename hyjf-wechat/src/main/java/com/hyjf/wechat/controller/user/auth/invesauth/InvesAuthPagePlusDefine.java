package com.hyjf.wechat.controller.user.auth.invesauth;

import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.wechat.base.BaseDefine;


public class InvesAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = InvesAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/invesauthpageplus";
	/** 缴费授权 */
    public static final String INVES_AUTH_ACTION = "/page";

    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/user/setting/authorization/result/success";
    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/user/setting/authorization/result/failed";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/invesAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/invesAuthBgreturn";
    
    public static final String AUTH_TYPE=AuthBean.AUTH_TYPE_AUTO_BID;
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/user/setting/quota/result/failed";
}
