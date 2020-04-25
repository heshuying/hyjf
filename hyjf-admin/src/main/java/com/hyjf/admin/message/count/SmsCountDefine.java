/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.message.count;

import com.hyjf.admin.BaseController;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author DELL
 * @version SmsCountDefine, v0.1 2018/5/15 11:27
 */
public class SmsCountDefine extends BaseController {

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 统计 @RequestMapping值
     */
    public static final String COUNT = "message/smsCount";


    /**
     * 短信统计列表页面 CONTROLLOR @RequestMapping值
     */
    public static final String MESSAGE_LIST_VIEW = "message/smsCount/list";

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "smsCountList";

    /**
     * 短信统计列表页面数据
     */
    public static final String LIST_SMS = "listSms";

    /**
     * 短信列表页面 CONTROLLOR @RequestMapping值
     */
    public static final String MESSAGE_FORM = "logForm";

    /**
     * 初始化接口
     */
    public static final String INIT_SMSCOUNT = "initSmsCount";
    /**
     * 取得部门信息
     */
    public static final String GET_CRMDEPARTMENT_LIST = "getCrmDepartmentList";

    /**
     * 根据业务需求导出相应的表格
     */
    public static final String EXPORT_SMS = "exportSms";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 文件导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
