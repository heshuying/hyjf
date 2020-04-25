/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.nifareportlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
public class NifaReportLogDefine extends BaseDefine{


    /** 互金协会报送日志@RequestMapping值 */
    public static final String REQUEST_MAPPING = "/datacenter/nifareportlog";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    public static final String DOWNLOADFILE = "downLoadFile";
    public static final String DOWNLOADFEEDBACKFILE = "downloadFeedbackFile";

    /** 列表画面 路径 */
    public static final String LIST_PATH= "datacenter/nifareportlog/nifareportlog";
    /** FROM */
    public static final String REPORTLOG_FORM = "nifareportlogForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "nifareportlog";


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
