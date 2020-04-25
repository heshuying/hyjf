package com.hyjf.admin.msgpush.history;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MessagePushHistoryDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/msgpush/history";
  
    /** 列表画面 路径 */
    public static final String LIST_PATH = "msgpush/history/history_list";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String FORM = "msgPushHistoryForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "msgpushhistory";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
}
