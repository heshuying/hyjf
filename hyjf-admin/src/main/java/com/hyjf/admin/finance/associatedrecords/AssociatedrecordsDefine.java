package com.hyjf.admin.finance.associatedrecords;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AssociatedrecordsDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/associatedrecords";

    /** 关联记录列表路径 */
    public static final String ASSOCIATED_RECORDS_LIST_PATH = "/finance/associatedrecords/associatedrecordsList";

    /** Form */
    public static final String ASSOCIATED_RECORDS_FORM = "associatedrecordsForm";

    /** 类名 */
    public static final String THIS_CLASS = AssociatedrecordsController.class.getName();

    /** 关联记录列表查询Action */
    public static final String ASSOCIATED_RECORDS_LIST = "getAssociatedrecordsList";

    /** 关联记录列表导出Action */
    public static final String ASSOCIATED_RECORDS_EXPORT = "associatedrecordsListExport";

    /** 权限 */
    public static final String PERMISSIONS = "associatedrecords";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 列表查询 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
