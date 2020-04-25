package com.hyjf.admin.message.log;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SmsLogDefine extends BaseDefine {

	/** 短信列表 CONTROLLOR @RequestMapping值 */
	public static final String MESSAGE_LIST = "message/smsLog";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 短信列表页面 CONTROLLOR @RequestMapping值 */
	public static final String MESSAGE_LIST_VIEW = "message/messagelist/list";

	/** 列表画面 @RequestMapping值 */
	public static final String TIME_INIT = "timeinit";
	/** 删除画面 @RequestMapping值 */
	public static final String DELETE = "delete";

	/** 短信列表页面 CONTROLLOR @RequestMapping值 */
	public static final String TIME_MESSAGE_LIST_VIEW = "message/timemessagelist/timelist";

	/** 短信列表页面 CONTROLLOR @RequestMapping值 */
	public static final String MESSAGE_FORM = "logForm";

	/** 短信列表页面数据 CONTROLLOR @RequestMapping值 */
	public static final String LIST_FORM = "logs";

	/** 短信列表 CONTROLLOR @RequestMapping值 */
	public static final String MESSAGE_PATH = "queryLogAction";

	/** 权限关键字 */
	public static final String PERMISSIONS = "smsLog";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 短信内容查看权限*/
	public static final String PERMISSIONS_SMSCONTENT_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SMSCONTENT_SHOW;
	
	/** 数据脱敏权限 */
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;

}
