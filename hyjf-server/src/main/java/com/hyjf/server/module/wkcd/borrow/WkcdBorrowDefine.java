package com.hyjf.server.module.wkcd.borrow;

import com.hyjf.server.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class WkcdBorrowDefine extends BaseDefine {
	/** 预注册Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/manager/borrow";

	/** 预注册信息列表@RequestMapping值 */
	public static final String WKCDBORROW_LIST_ACTION = "init";

	/** 跳至预注册信息页面 */
	public static final String GOTO_LIST = "/manager/wkcd/wkcdborrow/wkcdborrow";

	/** 类名 */
	public static final String THIS_CLASS = WkcdBorrowController.class.getName();

	/** 预注册FORM */
	public static final String WKCDBORROW_FORM = "wkcdBorrowForm";

	/** 导出Excel */
	public static final String EXPORT = "export";

	/** 权限 */
	public static final String PERMISSIONS = "wkcdBorrow";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
}
