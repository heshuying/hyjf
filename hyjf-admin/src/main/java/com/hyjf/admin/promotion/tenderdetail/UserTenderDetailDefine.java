package com.hyjf.admin.promotion.tenderdetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class UserTenderDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/promotion/usertenderdetail";
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "search";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 数据导出画面 @RequestMapping值 */
	public static final String EXPORT_USER_TENDER_DETAIL_ACTION = "exportUserTenderDetail";
	
	public static final String LIST_PATH = "promotion/tenderdetail/tenderDetailList";
	
	/** FROM */
	public static final String USER_TENDER_DETAIL_FORM = "userTenderDetailForm";
	/** 权限 */
	public static final String PERMISSIONS = "userTenderDetailList";

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
    
    /** 状态(不可用) */
    public static final String FLG_DISABLE = "1";

    /** 状态(可用) */
    public static final String FLG_AVTIVE = "0";
    
    public static final String THIS_CLASS = "UserTenderDetailController";
    
    /** 阈值 以月为基准 add by jijun 2018/03/15  */
    public static final int THRESHOLD_MONTH = 1;
    
}
