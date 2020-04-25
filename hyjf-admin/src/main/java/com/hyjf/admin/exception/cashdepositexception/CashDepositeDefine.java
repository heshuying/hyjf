package com.hyjf.admin.exception.cashdepositexception;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author lisheng
 * @version CashDepositeDefine, v0.1 2018/5/22 15:48
 */

public class CashDepositeDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/exception/cashdepositexception";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT_ACTION = "init";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "exception/cashdepositexception/cashdepositexception";

    /** 列表画面 @RequestMapping值 */
        public static final String SEARCH_ACTION = "/searchAction";

    /** FROM */
    public static final String ASSET_LIST_FORM = "assetListForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "assetlist";

    /** 转跳保证金处理界面**/
    public static final String TO_CASH_ACTION="ToModifyAction";

    /** 转跳保证金处理界面**/
    public static final String TO_MODIFY_LIST="exception/cashdepositexception/cashdeposithandle";

    public static  final  String MODIFY_ACTION="modifyAction1";
    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
}
