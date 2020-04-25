package com.hyjf.admin.msgpush.statics.plat;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MessagePushPlatStaticsDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/msgpush/statics/plat";
  
    /** 列表画面 路径 */
    public static final String LIST_PATH = "msgpush/statics/plat/plat_list";
    
    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/msgpush/statics/plat/init";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

	 /** 重发的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String FORM = "platStaticsForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "msgpushplatstatics";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;
	
	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
}
