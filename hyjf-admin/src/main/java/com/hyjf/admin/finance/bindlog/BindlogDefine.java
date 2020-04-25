package com.hyjf.admin.finance.bindlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BindlogDefine extends BaseDefine {
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/bindlog";

    /** 关联记录列表路径 */
    public static final String BIND_LOG_LIST_PATH = "/finance/bindlog/bindlogList";

    /** Form */
    public static final String BIND_LOG_FORM = "bindlogForm";

    /** 类名 */
    public static final String THIS_CLASS = BindlogController.class.getName();

    /** 关联记录列表查询Action */
    public static final String BIND_LOG_LIST = "getbindlogList";

    /** 关联记录列表导出Action */
    public static final String BIND_LOG_EXPORT = "bindlogListExport";

    /** 权限 */
    public static final String PERMISSIONS = "bindlog";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 列表查询 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
