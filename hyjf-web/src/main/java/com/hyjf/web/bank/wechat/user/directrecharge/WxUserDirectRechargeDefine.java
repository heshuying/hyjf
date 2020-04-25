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

package com.hyjf.web.bank.wechat.user.directrecharge;

import com.hyjf.web.BaseDefine;

public class WxUserDirectRechargeDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WxUserDirectRechargeController";
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/directrecharge";
	/** @RequestMapping值 */
	public static final String PAYMENT_AUTH_ACTION = "/page";
	
	/** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/return";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bgreturn";
}
