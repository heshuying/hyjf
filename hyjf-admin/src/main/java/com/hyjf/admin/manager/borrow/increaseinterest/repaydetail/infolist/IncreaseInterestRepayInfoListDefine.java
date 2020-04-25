package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息还款Define
 *
 * @ClassName IncreaseInterestRepayInfoListDefine
 * @author liuyang
 * @date 2017年1月4日 下午5:06:22
 */
public class IncreaseInterestRepayInfoListDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/increaseinterest/repaydetail/infolist";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "/manager/borrow/increaseinterest/repaydetail/infolist/infolist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表检索画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 类名 */
	public static String THIS_CLASS = IncreaseInterestRepayInfoListController.class.getName();

	/** 重定向路径 */
	public static String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;

	/** Form */
	public static final String FORM = "repayInfoForm";

	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";

	/** 权限 */
	/*public static final String PERMISSIONS = "repayDetail";*/

	/** 导出权限 */
	/*public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;*/
}
