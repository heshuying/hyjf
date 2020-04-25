package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息还款明细Define
 * 
 * @ClassName IncreaseInterestRepayDetailDefine
 * @author liuyang
 * @date 2016年12月29日 上午10:37:39
 */
public class IncreaseInterestRepayDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/increaseinterest/repaydetail";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/increaseinterest/repaydetail/increaseinterestrepaydetaillist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 重定向路径 */
	public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;

	/** 检索画面 @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";

	/** 还款明细详情 @RequestMapping*/
	public static final String INFO_LIST_ACTION = "infoListAction";

	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";

	/** 类名 */
	public static final String THIS_CLASS = IncreaseInterestRepayDetailController.class.getName();

	/** Form */
	public static final String REPAY_FORM = "repayDetailForm";

	/** 权限 */
	public static final String PERMISSIONS = "repayDetail";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
