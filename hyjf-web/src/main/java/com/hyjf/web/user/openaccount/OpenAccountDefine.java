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

package com.hyjf.web.user.openaccount;

import com.hyjf.web.BaseDefine;

public class OpenAccountDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/openaccount";
	/** @RequestMapping值 */
	public static final String INIT_OPENACCOUNT_ACTION = "/init";
	/** @RequestMapping值 */
	public static final String OPENACCOUNT_ACTION = "/open";
	/** @RequestMapping值 */
	public static final String CORP_OPEN_ACCOUNT_ACTION = "/openCorp";
	/** @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** @RequestMapping值 */
	public static final String RETURN_ASY_ACTION = "/bigreturn";
	/** @RequestMapping值 */
	public static final String CORP_OPEN_ACCOUNT_CALLBACK = "/openCorpAccountCallBack";
	/** chinapnrForm值 */
	public static final String CHINAPNR_FORM = "chinapnrForm";
	
	/** JSP 汇付天下跳转画面 */
	public static final String INIT_OPENACCOUNT_PATH = "/user/openaccount/initOpenAccount";
	/** JSP 汇付天下跳转画面 */
	public static final String OPENACCOUNT_SUCCESS_PATH = "/user/openaccount/openAccountSuccess";
	/** JSP 回调画面 */
	public static final String OPENACCOUNT_ERROR_PATH = "/user/openaccount/openAccountError";

	/** 当前controller名称 */
	public static final String THIS_CLASS = OpenAccountController.class.getName();
    
}
