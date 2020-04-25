package com.hyjf.admin.finance.pushMoney;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PushMoneyManageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/pushMoney";

	/** 提成管理 -列表画面 路径 */
	public static final String LIST_PATH = "finance/pushMoney/pushMoney_list";

	/** 资金管理- 提成列表画面的路径 */
	public static final String PUSHMONEYLIST_PATH = "finance/pushMoneyList/pushMoneyList";

	/** FROM */
	public static final String PUSHMONEY_FORM = "pushMoneyForm";
	/** 提成管理 列表    */
	public static final String PUSHMONEY_LIST = "pushMoney_list";
	/** 提成管理 列表 c查询条件    */
	public static final String PUSHMONEY_LIST_WITHQ = "searchAction";

    /** 资金管理- 提成列表    */
    public static final String PUSHMONEYLIST = "pushMoneyList";

	/** 计算提成 */
    public static final String CALCULATE_PUSHMONEY = "calculatePushMoneyAction";

	/** 发提成 */
	public static final String CONFIRM_PUSHMONEY = "confirmPushMoneyAction";

    /** 导出列表 @RequestMapping值 */
    public static final String EXPORT_PUSHMONEY_ACTION = "exportPushMoneyExcelAction";

    /** 导出列表 @RequestMapping值 */
    public static final String EXPORT_PUSHMONEY_DETAIL_ACTION = "exportPushMoneyDetailExcelAction";

    /** 导出列表 @RequestMapping值 具有 组织机构查看权限*/
    public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

    // 提成管理权限
	/** 查看权限 */
	public static final String PERMISSIONS = "pushMoneyManage";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_PUSHMONEY_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    /** 计算提成权限 */
    public static final String PERMISSIONS_PUSHMONEY_CALCULATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CALCULATE;

    // 提成列表权限
    /** 查看权限 */
    public static final String PERMISSIONS_DETAIL = "pushMoneyDetail";

    /** 查看权限 */
    public static final String PERMISSIONS_DETAIL_VIEW = PERMISSIONS_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_DETAIL_PUSHMONEY_EXPORT = PERMISSIONS_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;



    /** 发提成权限 */
    public static final String PERMISSIONS_DETAIL_PUSHMONEY_CONFIRM = PERMISSIONS_DETAIL + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;

    // 资金管理- 提成列表权限
    /** 查看权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST = "pushMoneyList";

    /** 查看权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_VIEW = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_EXPORT = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

    /** 发提成权限 */
    public static final String PERMISSIONS_PUSHMONEYLIST_PUSHMONEY_CONFIRM = PERMISSIONS_PUSHMONEYLIST + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;


}





