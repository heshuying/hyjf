package com.hyjf.admin.msgpush.notices;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MessagePushNoticesDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/msgpush/notices";
  
    /** 列表画面 路径 */
    public static final String LIST_PATH = "msgpush/notices/notices_list";

    /** 画面迁移 路径 */
    public static final String INFO_PATH = "msgpush/notices/noticesInfo";
    
    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/msgpush/notices/init";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "msgpush/notices/init";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";
    
    /**启用、禁用的 @RequestMapping值 */
    public static final String STATUS_ACTION = "statusAction";

	public static final String UPLOAD_FILE = "uploadFile";
	 /** 校验数据的 @RequestMapping值 */
	public static final String CHECK_ACTION = "checkAction";
	 /** 校验是否是url的 @RequestMapping值 */
	public static final String CHECKURL_ACTION = "checkUrlAction";
	 /** 校验是否是电话号码s的 @RequestMapping值 */
	public static final String CHECKMOBILES_ACTION = "checkMobilesAction";
    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String FORM = "msgPushNoticesForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "msgpushnotices";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 转发权限 */
	public static final String PERMISSIONS_RESEND = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_RESEND;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
}
