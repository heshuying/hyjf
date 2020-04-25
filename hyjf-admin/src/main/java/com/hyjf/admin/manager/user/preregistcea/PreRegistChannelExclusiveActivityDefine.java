package com.hyjf.admin.manager.user.preregistcea;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PreRegistChannelExclusiveActivityDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/preregistchannelexclusiveactivity";
	
	/** @RequestMapping值 */
	public static final String REGIST_LIST_ACTION = "ini";
	
	/** @RequestMapping值 */
    public static final String REGIST_UPDATE_ACTION = "update";
	
	/** @RequestMapping值 */
	public static final String REGIST_INI_UPDATE_ACTION = "iniUpdate";
	
	/** @RequestMapping值 */
    public static final String REGIST_SAVE_ACTION = "save";
    
	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_REGIST_ACTION = "export";
	
	/** 用户预注册页面 */
    public static final String REGIST_LIST_PATH = "manager/users/preregistcea/preregistchannelexclusiveactivity";

	/** 查看权限 */
	public static final String PERMISSIONS = "preregistchannelexclusiveactivity";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 编辑权限 */
    public static final String PERMISSIONS_UPDATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_UPDATE;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 类名*/
	public static final String THIS_CLASS = PreRegistChannelExclusiveActivityController.class.getName();
}
