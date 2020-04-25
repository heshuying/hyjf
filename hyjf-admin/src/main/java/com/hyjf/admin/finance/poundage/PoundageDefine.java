package com.hyjf.admin.finance.poundage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PoundageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/poundage";

	/** 列表画面  */
	public static final String POUNDAGE_LIST = "searchAction";
    
    /** 导出信息  */
    public static final String POUNDAGE_EXPORT = "exportAction";

    /** 导出详情  */
    public static final String POUNDAGE_DETAIL_EXPORT = "detail/exportAction";

    /** 分账信息 */
    public static final String DETAIL_ACTION = "detailAction";

    /** 分账 */
    public static final String TRANSFER_ACTION = "transferAction";

    /** 审核 */
    public static final String TRANSFER_AUDIT = "auditAction";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "finance/poundage/poundageList";

    /** 分账画面 路径 */
    public static final String DETAIL_PATH ="finance/poundage/poundageDetail";

	/** FORM */
	public static final String POUNDAGE_FORM = "poundageForm";
	

    
    /** 权限 */
    public static final String PERMISSIONS = "poundage";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 搜索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 分账权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 审核权限 */
    public static final String PERMISSIONS_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUDIT;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
