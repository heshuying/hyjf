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

package com.hyjf.api.surong.user.bankopen;

import com.hyjf.base.bean.BaseDefine;

public class OpenDefine extends BaseDefine {

	public static final String CONTROLLER_NAME = "BankOpenController";
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/bankopen";
	/** @RequestMapping值 */
	public static final String BANKOPEN_SENDCODE_ACTION = "/sendCode";
	/** @RequestMapping值 */
	public static final String BANKOPEN_OPEN_ACTION = "/open";
	
	public static final String BANKOPEN_OPEN_SUCESS_ACTION = "/openSuccess";
	
	public static final String BANKOPEN_OPEN_ACCOUNT_ACTION = "/openAccount";
	/** 开户画面路径 @RequestMapping值 */
	public static final String BANKOPEN_OPEN_PATH = "/bankopen/open_account";
	
	/** 开户成功画面路径 */
	public static final String BANKOPEN_OPEN_SUCCESS_PATH = "/bankopen/open_success";
	/** 开户失败画面路径*/
	public static final String BANKOPEN_OPEN_ERROR_PATH = "/bankopen/open_error";

	/** 失败结果@RequestMapping值 */
	public static final int ERROR_FAIL = 1;

	/** 成功结果@RequestMapping值 */
	public static final int ERROR_SUCCESS = 0;
	
	public static final String MESSAGE = "message";
	public static final String STATUSDESC = "statusDesc";
	
	
	
	public static final String MOBILE = "phone";
	public static final String USER_ID = "userId";
	public static final String LOGORDERID = "logOrderId";
	public static final String SIGN = "sign";
	public static final String TOKEN = "token";
	public static final String PLATFORM = "platform";
}
