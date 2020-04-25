package com.hyjf.admin.manager.borrow.increaseinterest.repayplan;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息还款计划Define
 * 
 * @ClassName IncreaseInterestRepayPlanDefine
 * @author liuyang
 * @date 2016年12月29日 上午9:10:07
 */
public class IncreaseInterestRepayPlanDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/increaseinterest/repayplan";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/increaseinterest/repayplan/increaseinterestrepayplanlist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 重定向 @RequestMapping */
	public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;

	/** 还款计划详情 @RequestMapping */
	public static final String REPAY_PLAN_INFO_ACTION = "repayPlanDetailAction";

	/** 检索画面 @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";

	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";

	/** 还款明细 @RequestMapping */
	public static final String REPAY_DETAIL_ACTION = "repayDetailAction";

	/** 类名 */
	public static final String THIS_CLASS = IncreaseInterestRepayPlanController.class.getName();

	/** Form */
	public static final String REPAY_FORM = "repayPlanForm";

	/** 权限 */
	public static final String PERMISSIONS = "repay";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详情权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
