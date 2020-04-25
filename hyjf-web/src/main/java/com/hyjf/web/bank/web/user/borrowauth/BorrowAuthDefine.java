package com.hyjf.web.bank.web.user.borrowauth;

import com.hyjf.web.BaseDefine;

public class BorrowAuthDefine extends BaseDefine {

	/**  @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/borrowauth";

	/** 待授权*/
    public static final String NEED_AUTH_LIST_ACTION = "/need_auth";
    /** 已授权*/
    public static final String AUTHED_LIST_ACTION ="/authed";

    /** 用户受托支付申请请求 */
    public static final String AUTH_PAGE_ACTION = "/trustee_pay";

    /**受托支付申请请求失败页面*/
    public static final String BORROW_AUTH_ERROR_PATH = "/bank/user/trusteePay/trustee_pay_fail";
    
    /**受托支付申请请求成功页面*/
    public static final String BORROW_AUTH_SUCCESS_PATH = "/bank/user/trusteePay/trustee_pay_success";

    /**受托支付同步地址*/
    public static final String RETURL_SYN_ACTION = "/trusteePayReturn";
    
    /**受托支付异步地址*/
    public static final String RETURL_ASY_ACTION = "/trusteePayBgreturn";

}
