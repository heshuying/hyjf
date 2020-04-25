/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.certreportlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;


public class CertReportLogDefine extends BaseDefine{


    /** 报送日志@RequestMapping值 */
    public static final String REQUEST_MAPPING = "/datacenter/certreportlog";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 列表画面 路径 */
    public static final String LIST_PATH= "datacenter/certreportlog/certreportlog";
    /** FROM */
    public static final String REPORTLOG_FORM = "form";

    /** 查看权限 */
    public static final String PERMISSIONS = "certlog";


    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

    /** 数据同步 @RequestMapping值 */
    public static final String DATA_SYN = "datasyn";

    /** 数据同步画面 路径 */
    public static final String DATA_SYN_PATH= "datacenter/datasyn/datasyn";

    /** 进行数据同步 @RequestMapping值 */
    public static final String DO_DATA_SYN = "dodatasyn";

}
