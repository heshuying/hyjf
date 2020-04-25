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

package com.hyjf.web.bank.web.user.bankopen.encryptpage;

import com.hyjf.web.BaseDefine;

public class BankOpenEncryptPageDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "BankOpenEncryptPageController";
	
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/bankopenencrypt";
	/** @RequestMapping值 */
	public static final String BANKOPEN_INIT_ACTION = "/init";
	/** @RequestMapping值 */
	public static final String BANKOPEN_OPEN_ACTION = "/open";
	public static final String BANKOPEN_INIT_PATH = "/bank/user/bankopenep/open";
	public static final String BANKOPEN_SUCCESS_ACTION = "/bank/user/transpassword/setPassword.do";
	public static final String BANKOPEN_ERROR_PATH = "/bank/user/bankopenep/error";
	public static final String BANKOPEN_ERROR_SUCCESS = "/bank/user/bankopenep/success";
	public static final String BANKOPEN_ERROR_SET_PASSWORD = "/bank/user/bankopenep/setPassword";
	/** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/bankOpenReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bankOpenBgreturn";

}
