package com.hyjf.admin.whereaboutspage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class WhereaboutsPageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/whereaboutspage";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "whereaboutspage/whereaboutspageList";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "whereaboutspage/whereaboutspageInit";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** 列表画面 @RequestMapping值 */
    public static final String CHECK_UTMID_ACTION = "checkUtmId";
    /** 列表画面 @RequestMapping值 */
    public static final String CHECK_REFERRER_ACTION = "checkReferrer";
    
    
    public static final String UPLOAD_FILE = "uploadFile";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	/** 迁移到详细画面 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";
    /** 状态更新  @RequestMapping值 */
    public static final String STATUS_ACTION = "statusAction";
    /** 迁移到详细画面 @RequestMapping值 */
    public static final String UPDATE_INFO_ACTION = "updateInfoAction";
    
    
    /** 用户有效性校验 @RequestMapping值  */
    public static final String CHECK_USER_ACTION = "checkUserAction";
	/** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";
	/** FROM */
	public static final String FORM = "whereaboutsPageForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "WHEREABOUTSPAGE";
	
	

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
}
