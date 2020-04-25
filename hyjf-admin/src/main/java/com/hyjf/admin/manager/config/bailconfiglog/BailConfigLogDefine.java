package com.hyjf.admin.manager.config.bailconfiglog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BailConfigLogDefine extends BaseDefine {

    /** 保证金日志 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/bailconfiglog";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "/manager/config/bailconfiglog/bailconfiglog";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 列表画面 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String BAILCONFIG_FORM = "bailConfigLogForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "bailConfigLog";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
    /** PopUp画面成功提交返回值Key */
    public static final String SUCCESS = "success";
}
