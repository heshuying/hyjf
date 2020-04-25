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

package com.hyjf.web.bank.web.user.bankopen;

import com.hyjf.web.BaseDefine;

public class BankOpenDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WebBankOpenController";
	
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/bankopen";
	/** @RequestMapping值 */
	public static final String BANKOPEN_INIT_ACTION = "/init";
	/** @RequestMapping值 */
	public static final String BANKOPEN_SENDCODE_ACTION = "/sendCode";
	/** @RequestMapping值 */
	public static final String BANKOPEN_OPEN_ACTION = "/open";
	/** JSP 汇付天下跳转画面 */
	public static final String BANKOPEN_INIT_PATH = "/bank/user/bankopen/open";
	/** JSP 汇付天下跳转画面 */
	public static final String BANKOPEN_SUCCESS_ACTION = "/bank/user/transpassword/setPassword.do";
	/** JSP 回调画面 */
	public static final String BANKOPEN_ERROR_PATH = "/bank/user/bankopen/error";
	
	public static final String BANKOPEN_ERROR_SUCCESS = "/bank/user/bankopen/success";

	public static final String MOBILE_CHECK_ACTION = "mobileCheck";
	
	/** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/bankOpenReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bankOpenBgreturn";

}
