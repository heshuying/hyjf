package com.hyjf.admin.exception.fdd.certificateauthorityexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * CA认证异常Define
 *
 * @author liuyang
 */
public class CertificateAuthorityExceptionDefine extends BaseDefine {

    /**
     * 权限 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/exception/fdd/certificateauthorityexception";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 列表检索画面 @RequestMapping值
     */
    public static final String SEARCH = "searchAction";

    /**
     * 异常处理更新Action @RequestMapping值
     */
    public static final String MODIFY = "modifyAction";
    /**
     * CA认证异常列表 @RequestMapping
     */
    public static final String LIST_PATH = "/exception/certificateauthorityexception/certificateauthorityexceptionlist";

    /**
     * FORM
     */
    public static final String FORM = "CAExceptionForm";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS = "CAException";

    /**
     * 查看权限 @RequstMapping
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 修改权限 @RequestMapping
     */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /**
     * 检索权限
     */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /**
     * 文件导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
