package com.hyjf.admin.manager.user.certificateauthority;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * CA认证记录Define
 *
 * @author liuyang
 */
public class FddCertificateAuthorityDefine extends BaseDefine {

    /**
     * 权限 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/certificateauthority";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 列表检索画面 @RequestMapping值
     */
    public static final String SEARCH = "searchAction";

    /** 导出数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "/exportAction";

    /**
     * CA认证异常列表 @RequestMapping
     */
    public static final String LIST_PATH = "manager/users/certificateauthority/certificateauthoritylist";

    /**
     * FORM
     */
    public static final String FORM = "CertificateAuthorityFrom";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS = "calist";

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
