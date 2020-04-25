package com.hyjf.admin.manager.hjhplan.reinvestdebt;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HjhReInvestDebtDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "manager/hjhreinvestdebt";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/hjhreinvestdetail/hjhreinvestdebt";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** FROM */
	public static final String REINVESTDEBT_FORM = "reinvestdebtForm";
	/** 资金明细 列表 */
	public static final String REINVESTDEBT_LIST = "hjhreinvestdebt_list";

	/** 导出资金明细列表 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 查看权限 */
	public static final String PERMISSIONS = "plancapitallist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
