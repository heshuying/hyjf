package com.hyjf.admin.exception.bankdebtend;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankDebtEndDefine extends BaseDefine {
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bankdebtend";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/bankdebtend/bankdebtendlist";
	/** 类名 */
	public static final String THIS_CLASS = BankDebtEndController.class.getName();
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** 更新 @RequestMapping值 */
	public static final String MODIFY_ACTION = "modifyAction";
	/** 恢复 @RequestMapping值 */
	public static final String RECOVERY_ACTION = "recovery";

	/** FROM */
	public static final String FORM = "bankDebtEndForm";
	/** 权限 */
	public static final String PERMISSIONS = "bankdebtend";
	/** 查看权限 @RequstMapping */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 检索权限 @RequestMapping */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 修改权限 @RequestMapping */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;
	
	/** ------批次结束债权需求 start  ----- */
	/** 列表画面 @RequestMapping值 */
	public static final String OLD_INIT = "oldinit";
	/** 列表画面 路径 */
	public static final String NEW_LIST_PATH = "exception/bankdebtend/newbankdebtendlist";
	/** 检索 @RequestMapping值 */
	public static final String SEARCH_NEW_ACTION = "searchNewAction";
	/** 同步 @RequestMapping值 */
	public static final String MODIFY_NEW_ACTION = "modifyNewAction";
	/** 更新 @RequestMapping值 */
	public static final String CHANGE_NEW_ACTION = "changeNewAction";
	/** ------批次结束债权需求 end  ----- */
}
