package com.hyjf.admin.message.config;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SmsConfigDefine extends BaseDefine {
	
	
	/** 列表请求 @RequestMapping值 */
	public static final String INIT = "init";
	
	
	/** 配置页面 @RequestMapping值 */
	public static final String INIT_PATH = "message/config/messageConfig";
	
	/** 页面入口@RequestMapping值 */
	public static final String CONFIG = "message/messageConfig";
	
	
	/** 页面入口@RequestMapping值 */
	public static final String INFO_FORM = "conform";
	
	
	/** 新增@RequestMapping值 */
	public static final String ADD_ACTION = "addAction";
	
	/** 修改@RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 权限关键字 */
	public static final String PERMISSIONS = "messageConfig";
	
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
}
