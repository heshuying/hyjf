package com.hyjf.admin.finance.directionaltransfer;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DirectionaltransferDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/directionaltransfer";

    /** 定向转账列表路径 */
    public static final String DIRECTIONAL_TRANSFER_LIST_PATH = "/finance/directionaltransfer/directionaltransferList";

    /** Form */
    public static final String DIRECTIONAL_TRANSFER_FORM = "directionaltransferForm";

    /** 类名 */
    public static final String THIS_CLASS = DirectionaltransferController.class.getName();

    /** 列表查询Action */
    public static final String DIRECTIONAL_TRANSFER_LIST = "getDirectionalTransferList";

    /** 定向转账导出 */
    public static final String DIRECTIONAL_TRANSFER_EXPORT = "directionalTransferListExport";

    /** 权限 */
    public static final String PERMISSIONS = "directionaltransfer";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 列表查询 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
