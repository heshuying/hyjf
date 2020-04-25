package com.hyjf.admin.finance.rechargefee;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RechargeFeeDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/rechargefee";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "finance/rechargefee/rechargefee_list";
    /** FROM */
    public static final String RECHARGE_FEE_FORM = "rechargeFeeForm";
    /** 账户管理 列表    */
    public static final String INIT = "init";
    /** 账户管理 列表 c查询条件    */
    public static final String SEARCH_ACTION= "searchAction";
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 查看权限 */
    public static final String PERMISSIONS = "rechargefee";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    public static final String STATUS_SUCCESS = "1";

}
