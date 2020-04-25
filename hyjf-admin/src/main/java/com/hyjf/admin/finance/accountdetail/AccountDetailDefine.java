package com.hyjf.admin.finance.accountdetail;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccountDetailDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/accountdetail";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "finance/accountdetail/accountdetail_list";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "finance/accountdetail/accountdetail_info";

	/** FROM */
	public static final String ACCOUNTDETAIL_FORM = "accountdetailForm";
	/** 资金明细 列表 */
	public static final String ACCOUNTDETAIL_LIST = "accountdetail_list";
	
	/** 资金明细 列表 */
	public static final String SEARCH_ACTION = "searhAction";

	/** 20170120还款后,交易明细修复 */
	public static final String ACCOUNTDETAIL_DATA_REPAIR = "accountdetailDataRepair";

	/** 导出资金明细列表 @RequestMapping值 */
	public static final String EXPORT_ACCOUNTDETAIL_ACTION = "exportAccountDetailsExcel";

	/** 查看权限 */
	public static final String PERMISSIONS = "accountdetail";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 文件导出权限 */
	public static final String PERMISSIONS_ACCOUNTDETAIL_EXPORT = PERMISSIONS + StringPool.COLON
			+ ShiroConstants.PERMISSION_EXPORT;

	/** 20170120还款后,交易明细修复权限 */
	public static final String PERMISSIONS_ACCOUNTDETAIL_DATA_REPAIR = PERMISSIONS + StringPool.COLON
			+ ShiroConstants.PERMISSION_DATA_REPAIR;
}
