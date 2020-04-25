package com.hyjf.admin.exception.bankrepayfreezeorg;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankRepayFreezeOrgDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bankRepayFreezeOrg";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/repayfreezeorg/bankfreezerepay_org";

	/** 银行冻结资金撤销跳转初始化 */
	public static final String INIT_ACTION = "init";

	/** 银行对账开始Action */
	public static final String SEARCH_ACTION = "searchAction";

	/** 处理*/
	public static final String PROCESS = "processAction";

	/** 查看*/
	public static final String CHECK = "checkAction";

	/** FROM */
	public static final String BANKREPAY_FORM = "bankRepayForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "bankrepayFreezeOrg";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PERMISSION_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
