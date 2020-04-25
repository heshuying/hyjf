package com.hyjf.admin.exception.bankcard;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankCardExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bankcardexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/bankcardexception/bankcardexception";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "initAction";
	
	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 查询数据 @RequestMapping值 */
	public static final String UPDATE_BANKCARDEXCEPTION_ACTION = "updateBankCardExceptionAction";

	/** 查询数据 @RequestMapping值 */
	public static final String UPDAYE_ALLBANKCARDEXCEPTION_ACTION = "updateAllBankCardExceptionAction";

	/** FROM */
	public static final String BANKCARDEXCEPTION_FORM = "bankCardExceptionForm";

	/** shiro权限 */
	public static final String PERMISSIONS = "bankcardexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSION_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 修改权限 */
	public static final String PERMISSION_MODIFYALL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYALL;
	
	/**
	 * 类名
	 */
	public static final String THIS_CLASS = BankCardExceptionController.class.getName();

}
