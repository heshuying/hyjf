package com.hyjf.admin.message.coupon.msglist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 优惠券站内信模块定义的常量
 */
public class SiteMsgLogDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/message/coupon/msglog";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "/message/coupon/msglist/msgList";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
	/** FROM */
	public static final String FORM = "siteMsgLogForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "sitemsglog";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
	
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
