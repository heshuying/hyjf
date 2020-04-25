package com.hyjf.admin.coupon.user;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 优惠券用户模块定义的常量
 */
public class CouponUserDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/coupon/user";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "coupon/user/couponUserList";

	/** 手动发布画面的路径 */
	public static final String DISTRIBUTE_PATH = "coupon/user/distribute";

	/** 手动发布画面的路径 */
	public static final String IMP_DISTRIBUTE_PATH = "coupon/user/impDistribute";
	
	/** 删除画面的路径 */
    public static final String DELETE_PATH = "coupon/user/delete";
    
    /** 详细画面的路径 */
    public static final String INFO_PATH = "coupon/user/tenderDetail";
    
    /** 详细画面的路径 */
    public static final String AUDIT_INIT_PATH = "coupon/user/couponUserAudit";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面查询 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
	
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	
	/** 数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 迁移到删除画面的  @RequestMapping值 */
    public static final String DELETEVIEW_ACTION = "deleteViewAction";
    
	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	
	/** 迁移到删除画面的  @RequestMapping值 */
    public static final String AUDIT_INIT_ACTION = "auditInitAction";
    /** 迁移到删除画面的  @RequestMapping值 */
    public static final String AUDIT_ACTION = "auditAction";
	
	/** 迁移到手动发布画面的@RequestMapping值  */
	public static final String DISTRIBUTEVIEW_ACTION = "distributeViewAction";
	
	/** 迁移到批量手动发布画面的@RequestMapping值  */
	public static final String IMP_DISTRIBUTEVIEW_ACTION = "impDistributeViewAction";
	
	/** 用户有效性校验 @RequestMapping值  */
	public static final String CHECK_USER_ACTION = "checkUserAction";
	
	/** 手动发布的@RequestMapping值  */
    public static final String DISTRIBUTE_ACTION = "distributeAction";
	
	/** 批量手动发布的@RequestMapping值  */
    public static final String UPLOAD_ACTION = "uploadAction";
    
    /** 加载优惠券配置的@RequestMapping值  */
    public static final String LOAD_COUPONCONFIG_ACTION = "loadCouponConfigAction";
    
    /** 校验优惠券配置是否有效 @RequestMapping值  */
    public static final String CHECK_COUPON_VALID_ACTION = "checkCouponValidAction";

	/** FROM */
	public static final String FORM = "couponUserForm";
	
	/**有效的活动列表 FROM*/
	public static final String ACTIVELIST_FORM = "activeListForm";
	
	/**优惠券配置列表 FROM*/
	public static final String CONFIG_FORM = "configForm";
	
	/** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** 查看权限 */
	public static final String PERMISSIONS = "couponuser";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
	
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
    
    /** 导入权限 */
    public static final String PERMISSIONS_IMPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_IMPORT;

}
