/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevelconfiglog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 风险测评信用评级配置logDefine
 *
 * @author liuyang
 * @version BorrowLevelConfigLogDefine, v0.1 2018/11/29 10:42
 */
public class BorrowLevelConfigLogDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "manager/config/evaluationconfig/borrowlevelconfiglog";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/config/evaluationconfig/borrowlevelconfig/borrowlevelconfigloglist";

    /** 列表画面 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** FROM */
    public static final String BORROW_LEVEL_CONFIG_LOG_FORM = "borrowLevelConfigLogForm";

    /** 权限关键字 */
    public static final String PERMISSIONS = "borrowLevelConfigLog";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
}
