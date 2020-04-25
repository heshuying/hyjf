package com.hyjf.admin.manager.user.regist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RegistDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/regist";

	/** 用户注册记录 */
	public static final String REGIST_LIST_PATH = "manager/users/registlist/registList";

	/** 会员注册信息列表画面 @RequestMapping值 */
	public static final String REGIST_LIST_ACTION = "registlist";

	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_REGIST_ACTION = "exportregist";

	/** 用户注册列表FROM */
	public static final String REGIST_LIST_FORM = "registListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "registlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 数据脱敏权限 */
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 类名 */
	public static final String THIS_CLASS = RegistController.class.getName();
}
