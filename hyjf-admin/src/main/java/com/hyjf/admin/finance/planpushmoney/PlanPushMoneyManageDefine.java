package com.hyjf.admin.finance.planpushmoney;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanPushMoneyManageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/planPushMoney";

	/** 列表画面路径 */
	public static final String LIST_PATH = "/finance/planpushmoney/planpushmoneylist";

	/** 提成明细列表路径 */
	public static final String PUSH_MONEY_DETAIL_LIST_PATH = "/finance/planpushmoneydetail/planpushmoneydetaillist";

	/** 提成列表 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索Action @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";

	/** 计算提成 @RequestMapping值 */
	public static final String CALCULATE_PUSHMONEY = "calculatePushMoneyAction";

	/** 导出列表 @RequestMapping值 */
	public static final String EXPORT_PUSHMONEY_ACTION = "exportPushMoneyExcelAction";

	/** 提成明细列表 @RequestMapping值 */
	public static final String PLAN_PUSHMONEY_DETAIL_ACTION = "planPushMoneyDetail";

	/** 提成明细列表检索 @RequestMapping值 */
	public static final String PLAN_PUSHMONEY_DETAIL_SEARCH_ACTION = "planPushMoneyDetailSearch";

	/** 发提成 */
	public static final String CONFIRM_PUSHMONEY = "confirmPushMoneyAction";

	/** 导出列表 @RequestMapping值 */
	public static final String EXPORT_PUSHMONEY_DETAIL_ACTION = "exportPushMoneyDetailExcelAction";

	/** form */
	public static final String FORM = "planPushMoneyForm";

	/** 提成列表权限 */
	public static final String PERMISSIONS = "planPushMoneyManage";

	/** 查看权限 */
	public static final String PERMISSIONS_PUSHMONEY_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 计算提成权限 */
	public static final String PERMISSIONS_PUSHMONEY_CALCULATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CALCULATE;

	/** 文件导出权限 */
	public static final String PERMISSIONS_PUSHMONEY_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 提成明细权限 */
	public static final String PERMISSIONS_PUSHMONEY_DETAIL = "planPushMoneyDetail";

	/** 提成明细查看权限 */
	public static final String PERMISSIONS_PUSHMONEYDETAIL_VIEW = PERMISSIONS_PUSHMONEY_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 发提成权限 */
	public static final String PERMISSIONS_DETAIL_PUSHMONEY_CONFIRM = PERMISSIONS_PUSHMONEY_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;

	/** 文件导出权限 */
	public static final String PERMISSIONS_DETAIL_PUSHMONEY_EXPORT = PERMISSIONS_PUSHMONEY_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
