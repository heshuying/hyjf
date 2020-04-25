package com.hyjf.admin.exception.offline.recharge;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class OfflineRechargeDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/offline/recharge";
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "search";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 数据导出画面 @RequestMapping值 */
	public static final String EXPORT_OFFLINE_RECHARGE_ACTION = "exportOfflineRecharge";
	
	public static final String LIST_PATH = "offline-recharge/rechargeList";
	
	/** FROM */
	public static final String OFFLINE_RECHARGE_FORM = "rechargelistForm";
	/** 权限 */
	public static final String PERMISSIONS = "offline_rechargelist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
	
	/** 删除权限 */
    public static final String PERMISSIONS_RESETPWD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_RESETPWD;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 设置角色权限 */
    public static final String PERMISSIONS_AUTH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUTH;
    
    /** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    public static final String THIS_CLASS = "OfflineRechargeController";

}
