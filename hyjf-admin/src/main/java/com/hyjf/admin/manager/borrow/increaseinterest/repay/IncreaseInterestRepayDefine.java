package com.hyjf.admin.manager.borrow.increaseinterest.repay;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息还款信息Define
 * 
 * @ClassName IncreaseInterestRepayDefine
 * @author liuyang
 * @date 2016年12月28日 下午4:13:07
 */
public class IncreaseInterestRepayDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/increaseinterest/repay";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/increaseinterest/repay/increaseinterestrepaylist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 重定向 @RequestMapping值 */
	public static String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;
	/** 检索画面 @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";

	/** 跳转还款计划 @RequestMapping */
	public static final String REPAY_PLAN_ACTION = "repayPlanAction";

	/** 跳转到详情页面的Action @RequestMapping值 */
	public static final String TO_RECOVER_ACTION = "toRecoverAction";

	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";

	/** 类名 */
	public static final String THIS_CLASS = IncreaseInterestRepayController.class.getName();

	/** Form */
	public static final String REPAY_FORM = "repayForm";

	/** 权限 */
	public static final String PERMISSIONS = "repay";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
