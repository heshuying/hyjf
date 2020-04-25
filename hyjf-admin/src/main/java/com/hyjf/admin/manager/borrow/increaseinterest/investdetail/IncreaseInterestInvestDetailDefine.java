package com.hyjf.admin.manager.borrow.increaseinterest.investdetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息出借明细Define
 * 
 * @ClassName InvestDetailDefine
 * @author liuyang
 * @date 2016年12月28日 上午11:44:47
 */
public class IncreaseInterestInvestDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/increaseinterest/investdetail";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/increaseinterest/investdetail/investdetail";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索画面 @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";

	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";

	/** 类名 */
	public static final String THIS_CLASS = IncreaseInterestInvestDetailController.class.getName();

	/** Form */
	public static final String INVEST_DETAIL_FORM = "investDetailForm";

	/** 权限 */
	public static final String PERMISSIONS = "investDetail";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
