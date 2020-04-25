/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.bank.wechat.user.paymentauthpage;

import com.hyjf.web.BaseDefine;

public class WxPaymentAuthPagDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WxPaymentAuthPagController";
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/paymentauthpage";
	/** @RequestMapping值 */
	public static final String PAYMENT_AUTH_ACTION = "/page";
	
	/** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/bankOpenReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bankOpenBgreturn";
}
