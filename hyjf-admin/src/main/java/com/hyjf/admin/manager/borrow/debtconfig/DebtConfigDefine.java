package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtConfigDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/debtconfig";

	public static final String PAGE_MAPPING = "/manager/borrow/debtconfiglog";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面 @RequestMapping值 */
	public static final String PAGE_INIT = "pageInit";
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/debtconfig/debtconfiglog_list";
	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	public static final String RE_LIST_PATH = "redirect:/manager/borrow/debtconfig/init";
	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/debtconfig/debtconfig_info";
	/** 查看权限 */
	public static final String PERMISSIONS = "debtconfig";

	public static final String PERMISSIONS_LOG = "debtconfiglog";
	/** 关联记录列表导出Action */
	public static final String DEBTCONFIGLOG_EXPORT = "debtconfiglogExport";
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	public static final String PERMISSIONS_LOG_VIEW = PERMISSIONS_LOG + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String PERMISSIONS_LOG_EXPORT = PERMISSIONS_LOG + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;


	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	
}
