package com.hyjf.admin.finance.web;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class WebDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/web";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "finance/web/web_list";
	
	/** 余额查询 路径 */
	public static final String YUE_DETAILS_PATH = "finance/web/yueDetails";

	/** 详细画面的路径 */
//	public static final String INFO_PATH = "finance/web/web_info";

	/** FROM */
	public static final String WEB_FORM = "webForm";
	/** 网站收支 列表    */
	public static final String WEB_LIST = "web_list";
	/** 网站收支 列表 c查询条件    */
	public static final String WEB_LIST_WITHQ = "searchAction";
	
	/**
	 * 余额查询
	 */
	public static final String YUE_SEARCH_ACTION = "yueSearchAction";
    
    /** 导出网站收支  @RequestMapping值 */
    public static final String EXPORT_WEB_ACTION = "exportWeblistExcel";

	/** 导出列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

	/** 查看权限 */
	public static final String PERMISSIONS = "web";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 文件导出权限 */
    public static final String PERMISSIONS_WEB_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;
    
	
}





	