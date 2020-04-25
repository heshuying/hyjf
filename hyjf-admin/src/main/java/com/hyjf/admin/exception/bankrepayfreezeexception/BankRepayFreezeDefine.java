package com.hyjf.admin.exception.bankrepayfreezeexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankRepayFreezeDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bankRepayFreezeException";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/repayfreezeexception/bankfreezerepay";

	/** 银行冻结资金撤销跳转初始化 */
	public static final String INIT_ACTION = "init";

	/** 银行对账开始Action */
	public static final String SEARCH_ACTION = "searchAction";

	/** 银行对账开始Action */
	public static final String REPAYUNFREEZE_ACTION = "repayunfreezeAction";

	/** FROM */
	public static final String BANKREPAY_FORM = "bankRepayForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "bankrepayFreeze";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PERMISSION_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
