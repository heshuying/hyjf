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

package com.hyjf.web.bank.web.user.tender;

import com.hyjf.web.BaseDefine;

public class TenderDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WebTenderController";
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/tender";
	/** @RequestMapping值 */
	public static final String INVEST_ACTION = "/invest";
	/** @RequestMapping值 */
	public static final String INVEST_INFO_ACTION="/investInfo";
	/** @RequestMapping值 */
	public static final String INVEST_CHECK_ACTION = "/investCheck";
	/** 出借后同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/returnUrl";
	/** 出借后异步回调 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/bgreturnUrl";
	/** 出借成功@RequestMapping值 */
	public static final String INVEST_SUCCESS_PATH = "/bank/user/tender/tendersuccess";
	/** 出借失败@RequestMapping值 */
	public static final String INVEST_ERROR_PATH = "/bank/user/tender/tendererror";

}
