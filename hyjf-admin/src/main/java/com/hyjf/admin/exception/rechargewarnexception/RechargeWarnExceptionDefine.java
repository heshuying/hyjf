package com.hyjf.admin.exception.rechargewarnexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RechargeWarnExceptionDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/exception/rechargewarnexception";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "exception/rechargewarnexception/rechargewarnexception_list";

    /** FROM */
    public static final String RECHARGE_FORM = "form";

    /** 账户管理 列表 */
    public static final String INIT = "init";

    /** 账户管理 列表 c查询条件 */
    public static final String RECHARGE_LIST_WITHQ = "searchAction";

    /** FIX */
    public static final String RECHARGE_FIX = "rechargeFixAction";

    /** 查看权限 */
    public static final String PERMISSIONS = "rechargewarnex";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 修改权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 充值掉单修复权限 */
    public static final String PERMISSIONS_RECHARGE_EXCEPTION = PERMISSIONS + StringPool.COLON
            + ShiroConstants.PERMISSION_RECHARGE_EXCEPTION;
}
