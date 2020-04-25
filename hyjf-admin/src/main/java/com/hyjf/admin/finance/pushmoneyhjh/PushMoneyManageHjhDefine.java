package com.hyjf.admin.finance.pushmoneyhjh;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PushMoneyManageHjhDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/pushmoneyhjh";

	/** 资金管理- 提成列表画面的路径 */
	public static final String PUSHMONEYLIST_PATH = "finance/pushmoneyhjh/pushMoneyList";
	/**
	 * 确定显示信息页面
	 */
	public static final String PUSHMONEYSHOW_PATH = "finance/pushmoneyhjh/pushMoneyShow";

	/** FROM */
	public static final String PUSHMONEY_FORM = "pushMoneyForm";
	/** 提成管理 列表 c查询条件    */
	public static final String PUSHMONEY_LIST_WITHQ = "searchAction";

    /** 资金管理- 提成列表    */
    public static final String PUSHMONEYLIST = "pushMoneyList";
    
    /**校验发提成状态是不是已经发放    */
    public static final String CHECK_STATUS_ACTION = "checkStatusAction";

	/** 发提成 */
	public static final String CONFIRM_PUSHMONEY = "confirmPushMoneyAction";
	/** 发提成展示页面 */
	public static final String SHOW_PAGE_ACTION = "showPageAction";

    /** 导出列表 @RequestMapping值 */
    public static final String EXPORT_PUSHMONEY_DETAIL_ACTION = "exportPushMoneyDetailExcelAction";
    

    public static final String PERMISSIONS_PUSHMONEYLIST = "pushMoneyListHJH";

    /** 查看权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_VIEW = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_EXPORT = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    /** 发提成权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_CONFIRM = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;


}





