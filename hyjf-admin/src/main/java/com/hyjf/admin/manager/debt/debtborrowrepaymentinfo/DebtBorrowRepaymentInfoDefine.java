package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtBorrowRepaymentInfoDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/debt/debtborrowrepaymentinfo";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/debt/debtborrowrepaymentinfo/debtborrowrepaymentinfo";

	/** 初始化 */
	public static final String INIT = "init";

	/** 跳转到还款明细列表 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 转发路径 */
	public static final String FORWORD_LIST_PATH = "forward:"+REQUEST_MAPPING+"/"+INIT;
	
	/** 重定向路径 */
	public static final String RE_LIST_PATH = "redirect:"+REQUEST_MAPPING+"/"+INIT;

	/** 跳转到还款详情列表 */
	public static final String TO_XIANGQING_ACTION = "toXiangqingAction";
	
	/** 导出Excel表格Action */
	public static final String EXPORT_ACTION = "exportAction";

	/** FROM */
	public static final String REPAYMENTINFO_FORM = "debtBorrowRepaymentInfoForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtborrowrepay";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 还款权限 */
	public static final String PERMISSIONS_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_REPAY;

	/** 延期权限 */
	public static final String PERMISSIONS_AFTER_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AFTER_REPAY;
	
	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
