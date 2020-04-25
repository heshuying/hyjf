/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.operationlog;

import com.hyjf.admin.BaseDefine;

/**
 * @author yaoyong
 * @version UserOperationLogDefine, v0.1 2018/10/9 16:00
 */
public class UserOperationLogDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/operationlog";

    /** 列表画面 @RequestMapping值 */
    public static final String OPERATIONLOG_LIST_ACTION = "operationLog";

    /** 列表FORM */
    public static final String OPERATIONLOG_LIST_FORM = "operationLogListForm";

    /** 用户授权明细 */
    public static final String OPERATIONLOG_LIST_PATH = "manager/users/operationlog/useroperationlog";

    /** 操作日志表格导出 */
    public static final String EXPORT_OPERATIONLOG_ACTION = "exportoperationlog";
}
