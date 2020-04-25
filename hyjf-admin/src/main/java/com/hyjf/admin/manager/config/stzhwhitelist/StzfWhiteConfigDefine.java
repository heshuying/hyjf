package com.hyjf.admin.manager.config.stzhwhitelist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class StzfWhiteConfigDefine extends BaseDefine {

    /** 提成配置 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/stzhwhitelist";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/config/stzhwhitelist/stzhwhitelist";
    /** 提成配置 重定向 */
    public static final String RE_LIST_PATH = "redirect:/manager/config/stzhwhitelist/init";

    /** 列表画面迁移 */
    public static final String INFO_PATH = "manager/config/stzhwhitelist/stzhwhitelistInfo";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    public static final String ERROR = "stzfWhiteConfigError";
    
    public static final String ST_ERROR = "stzfWhiteConfigStNameError";
    

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";
    
    /** 姓名对应的信息 @RequestMapping值 */
    public static final String LOAD_NAME_CONFIG = "loadNadmeConfig";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String INSTCONFIG_FORM = "stzfConfigForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "stzfconfig";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
