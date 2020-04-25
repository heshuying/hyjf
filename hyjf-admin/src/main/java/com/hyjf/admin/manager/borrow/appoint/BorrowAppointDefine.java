package com.hyjf.admin.manager.borrow.appoint;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowAppointDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/appoint";

	/** 会员开户列表画面 @RequestMapping值 */
	public static final String APPOINT_LIST_ACTION = "appointList";
	
	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_APPOINT_ACTION = "exportAppoint";

	/** 用户开户列表FROM */
	public static final String APPOINT_LIST_FORM = "appointListForm";
	
	/** 用户开户记录 */
	public static final String APPOINT_LIST_PATH = "manager/borrow/appoint/appointList";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "appointlist";
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_OPEN_ACCOUNT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM_ACCOUNT;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 类名*/
	public static final String THIS_CLASS = BorrowAppointController.class.getName();

}
