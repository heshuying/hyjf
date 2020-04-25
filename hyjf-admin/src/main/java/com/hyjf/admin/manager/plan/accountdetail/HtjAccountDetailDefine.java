package com.hyjf.admin.manager.plan.accountdetail;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HtjAccountDetailDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htj/htjaccountdetail";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htj/htjaccountdetail/htjaccountdetail_list";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "htj/htjaccountdetail/htjaccountdetail_info";

	/** FROM */
	public static final String ACCOUNTDETAIL_FORM = "accountdetailForm";
	/** 资金明细 列表 */
	public static final String ACCOUNTDETAIL_LIST = "accountdetail_list";

	/** 导出资金明细列表 @RequestMapping值 */
	public static final String EXPORT_ACCOUNTDETAIL_ACTION = "exportAccountDetailsExcel";

	/** 查看权限 */
	public static final String PERMISSIONS = "htjaccountdetail";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 文件导出权限 */
	public static final String PERMISSIONS_ACCOUNTDETAIL_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
