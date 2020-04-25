package com.hyjf.wechat.controller.user.auth.creditauth;

import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.wechat.base.BaseDefine;


public class CreditAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = CreditAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/creditauthpageplus";
	/** 缴费授权 */
    public static final String CREDIT_AUTH_ACTION = "/page";

    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/user/setting/authorization/result/success";
    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/user/setting/authorization/result/failed";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/creditAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/creditAuthBgreturn";
    
    public static final String AUTH_TYPE=AuthBean.AUTH_TYPE_AUTO_CREDIT;
    
    

}
