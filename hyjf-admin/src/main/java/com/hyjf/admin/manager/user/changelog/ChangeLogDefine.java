package com.hyjf.admin.manager.user.changelog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ChangeLogDefine extends BaseDefine {

	/** 操作日志 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/changelog";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    public static final String EXPORT_CHANGELOG_ACTION = "exportChangeLog";
    
    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/users/changeloglist/changeLogList";
    
    /** FROM */
    public static final String CHANGELOG_FORM = "changeLogForm";
    
    public static final Integer CHANGELOG_TYPE_IDCARD = 3;
    
    public static final Integer CHANGELOG_TYPE_USERINFO = 2;

    public static final Integer CHANGELOG_TYPE_RECOMMEND = 1;
    // 合规四期 添加change_log类型 add by nxl Start
    //修改手机号
    public static final Integer CHANGELOG_TYPE_MOBILE = 4;
    //修改邮箱
    public static final Integer CHANGELOG_TYPE_EMAIL = 5;
    //修改用户角色
    public static final Integer CHANGELOG_TYPE_USERROLE = 6;
    //修改银行卡信息
    public static final Integer CHANGELOG_TYPE_CARDCHANGE= 7;
    // 合规四期 添加change_log类型 add by nxl End

    /** 权限关键字 */
    public static final String PERMISSIONS = "changeloglist";
    
    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    
    /** 文件导出权限 */
    public static final String PERMISSIONS_CHANGELOG_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /**
     * 显示脱敏数据
     */
    public static final String PERMISSION_HIDE_SHOW =  PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;

}
