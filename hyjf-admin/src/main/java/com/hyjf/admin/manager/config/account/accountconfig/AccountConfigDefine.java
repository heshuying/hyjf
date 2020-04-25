package com.hyjf.admin.manager.config.account.accountconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccountConfigDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/accountconfig";

    /** 列表路径 */
    public static final String LIST_PATH = "manager/config/account/accountconfig/accountconfigList";

    /** 详情路径 */
    public static final String INFO_PATH = "manager/config/account/accountconfig/accountconfigDetail";

    /** 列表画面@RequestMapping值 */
    public static final String INIT = "init";

    /** 检索 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 数据添加Action @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 数据更新Action @RequestMapping */
    public static final String UPDATE_ACTION = "updateAction";

    /**CheckAction*/
    public static final String CHECK_ACTION = "checkAction";

    /** FORM */
    public static final String ACCOUNT_CONFIG_FORM = "accountconfigForm";

    /** 类名 */
    public static final String THIS_CLASS = AccountConfigController.class.getName();

    /** 权限 */
    public static final String PERMISSIONS = "accountconfig";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
}
