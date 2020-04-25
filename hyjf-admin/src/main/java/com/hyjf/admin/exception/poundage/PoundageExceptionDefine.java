package com.hyjf.admin.exception.poundage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * com.hyjf.admin.exception.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
public class PoundageExceptionDefine extends BaseDefine {


    /**
     * 权限 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/poundage/exception";

    /**
     * 列表画面
     */
    public static final String POUNDAGE_EXCEPTION_LIST = "searchAction";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "exception/poundage/poundageExceptionList";

    /** 分账画面 路径 */
    public static final String DETAIL_PATH ="exception/poundage/poundageExceptionDetail";

    /** 分账信息 */
    public static final String DETAIL_ACTION = "detailAction";

    /** 分账 */
    public static final String TRANSFER_ACTION = "transferAction";

    /**
     * FORM
     */
    public static final String POUNDAGE_EXCEPTION_FORM = "poundageExceptionForm";


    /**
     * 权限名称
     */
    public static final String PERMISSIONS = "poundageException";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 查询权限
     */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /**
     * 分账权限
     */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
