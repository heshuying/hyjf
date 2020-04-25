package com.hyjf.admin.manager.debt.debtborrowrepayment;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtBorrowRepaymentDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/debt/debtborrowrepayment";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/debt/debtborrowrepayment/debtborrowrepayment";

	/** 列表画面 路径 */
	public static final String REPAY_PATH = "manager/debt/debtborrowrepayment/debtrepay/debtborrowrepay";

	/** 列表画面 路径 */
	public static final String DELAY_REPAY_PATH = "manager/debt/debtborrowrepayment/debtdelayrepay/debtdelayrepay";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 还款 @RequestMapping值 */
	public static final String INIT_REPAY_ACTION = "initRepayAction";

	/** 延期 @RequestMapping值 */
	public static final String INIT_DELAY_REPAY_ACTION = "initDelayRepayAction";

	/** 还款 @RequestMapping值 */
	public static final String REPAY_ACTION = "repayAction";

	/** 延期 @RequestMapping值 */
	public static final String DELAY_REPAY_ACTION = "delayRepayAction";

    /** 重新还款 @RequestMapping值 */
    public static final String RESTART_REPAY_ACTION = "restartRepayAction";

    /** 更新管理费 @RequestMapping值 */
    public static final String UPDATE_RECOVER_FEE_ACTION = "updateRecoverFeeAction";

	/** 转发路径 */
	public static final String FORWORD_LIST_PATH = "forward:" + REQUEST_MAPPING + "/" + INIT;

	/** 重定向路径 */
	public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 跳转到还款计划的Action @RequestMapping值 */
	public static final String REPAY_PLAN_ACTION = "repayPlanAction";
	
	/** 跳转到详情页面的Action @RequestMapping值 */
	public static final String TO_RECOVER_ACTION = "toRecoverAction";

	/** FROM */
	public static final String REPAYMENT_FORM = "debtBorrowRepaymentForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtborrowrepayment";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 还款权限 */
	public static final String PERMISSIONS_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_REPAY;

	/** 延期权限 */
	public static final String PERMISSIONS_AFTER_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AFTER_REPAY;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
