package com.hyjf.admin.message;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SmsCodeDefine extends BaseDefine {

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面 @RequestMapping值 */
	public static final String TIME_INIT = "timeinit";

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String MESSAGE = "/message/message";

	/** 发送短信 CONTROLLOR @RequestMapping值 */
	public static final String SEND_PATH = "message/sendMessage/sendMessage";
	/** 发送短信 CONTROLLOR @RequestMapping值 */
	public static final String SINGLE_SEND_PATH = "message/sendMessageSingle/sendMessageSingle";

	/** 查询用户 CONTROLLOR @RequestMapping值 */
	public static final String QUERYUSER_ACTION = "queryUserAction";

	/** 发送短信 CONTROLLOR @RequestMapping值 */
	public static final String SENDMESSAGE_ACTION = "sendMessageAction";

	/** 软件序列号 */
	public static final String SOFT_SERIALNO = "9SDK-EMY-0999-JBVLP";

	/** 自定义关键字(key值) */
	public static final String KEY = "286141";

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

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 单发短信 */
	public static final String PERMISSIONS_SINGLE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SINGLE;

	/** 群发短信 */
	public static final String PERMISSIONS_MASS = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MASS;

}
