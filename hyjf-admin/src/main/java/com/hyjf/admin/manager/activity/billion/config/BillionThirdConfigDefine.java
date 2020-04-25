package com.hyjf.admin.manager.activity.billion.config;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 十一月份活动
 * @author Michael
 */
public class BillionThirdConfigDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/billion/third/config";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/billion/billion_third_config";
    
    /** 画面迁移 路径 */
    public static final String INFO_PATH = "manager/activity/billion/billion_third_config_info";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";
    
    /** 资料上传 @RequestMapping值 */
	public static final String UPLOAD_FILE = "uploadFile";
	
    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";
    
    /** FROM */
    public static final String FORM = "billionThirdConfigForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
