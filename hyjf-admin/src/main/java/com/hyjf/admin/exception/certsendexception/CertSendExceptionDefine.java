/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.exception.certsendexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;


public class CertSendExceptionDefine extends BaseDefine{


    /** 报送日志@RequestMapping值 */
    public static final String REQUEST_MAPPING = "/exception/certsendexception";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    /**  */
    public static final String UPDATECOUNT = "updateCount";

    /** 列表画面 路径 */
    public static final String LIST_PATH= "exception/certsendexception/certerrorlog";
    /** FROM */
    public static final String REPORTLOG_FORM = "form";

    /** 查看权限 */
    public static final String PERMISSIONS = "certerrorlog";

    /**发送MQ*/
    public static final String SEND_MQ = "sendmq";
    /**发送MQ页面*/
    public static final String SEND_MQ_PATH = "exception/certsendexception/sendMQ";

    public static final String DO_SEND_MQ = "dosendmq";
    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
