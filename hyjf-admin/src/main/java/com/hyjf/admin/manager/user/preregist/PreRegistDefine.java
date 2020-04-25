package com.hyjf.admin.manager.user.preregist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PreRegistDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/preregist";
	
	/** @RequestMapping值 */
	public static final String REGIST_LIST_ACTION = "preregistlist";
	
	/** @RequestMapping值 */
	public static final String REGIST_UPDATE_ACTION = "updatepreregistlist";
	
	/** @RequestMapping值 */
    public static final String REGIST_SAVE_ACTION = "savepreregistlist";
	
	/** 用户预注册页面 */
    public static final String REGIST_LIST_PATH = "manager/users/preregist/preregistList";
    
    /** 用户预注册编辑页面 */
    public static final String REGIST_UPDATE_PATH = "manager/users/preregist/preregistupdate";
    
	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_REGIST_ACTION = "exportpreregist";

	/** 查看权限 */
	public static final String PERMISSIONS = "preregist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 编辑权限 */
    public static final String PERMISSIONS_UPDATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_UPDATE;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 类名*/
	public static final String THIS_CLASS = PreRegistController.class.getName();
}
