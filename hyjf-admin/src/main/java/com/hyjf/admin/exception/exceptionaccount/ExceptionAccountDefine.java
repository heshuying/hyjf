package com.hyjf.admin.exception.exceptionaccount;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ExceptionAccountDefine extends BaseDefine {
	/** 预注册Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/exception/exceptionaccount";

	/** 预注册信息列表@RequestMapping值 */
	public static final String EXCEPTIONACCOUNT_LIST_ACTION = "init";

	/** 跳至预注册信息页面 */
	public static final String GOTO_LIST = "/exception/exceptionaccount/exceptionaccount";

	/** 类名 */
	public static final String THIS_CLASS = ExceptionAccountController.class.getName();

	/** 预注册FORM */
	public static final String EXCEPTIONACCOUNT_FORM = "exceptionAccountForm";

	/** 导出Excel */
	public static final String EXPORT = "export";
	
	/** 账户信息同步 */
	public static final String SYNC = "sync";

	/** 权限 */
	public static final String PERMISSIONS = "exceptionaccount";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
}
