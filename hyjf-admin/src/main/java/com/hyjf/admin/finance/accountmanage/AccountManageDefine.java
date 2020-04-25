package com.hyjf.admin.finance.accountmanage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccountManageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/accountmanage";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "finance/accountmanage/accountmanage_list";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "finance/accountdetail/accountdetail_list";

	/** FROM */
	public static final String ACCOUNTMANAGE_FORM = "accountmanageForm";
	/** 账户管理 列表    */
	public static final String ACCOUNTMANAGE_LIST = "accountmanage_list";
	/** 资金明细 列表    */
	public static final String ACCOUNTDETAIL_LIST = "accountdetail_list";

    /** 导出账户列表 @RequestMapping值 */
    public static final String EXPORT_ACCOUNTMANAGE_ACTION = "exportAccountsExcel";

	/** 导出列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

    /** 更新资金明细    */
    public static final String UPDATE_BALANCE_ACTION = "updateBalanceAction";

	/** 查看权限 */
	public static final String PERMISSIONS = "accountmanage";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 文件导出权限 */
    public static final String PERMISSIONS_ACCOUNTMANAGE_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

    /** 更新权限 */
    public static final String PERMISSIONS_UPDATE_BALANCE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

}





