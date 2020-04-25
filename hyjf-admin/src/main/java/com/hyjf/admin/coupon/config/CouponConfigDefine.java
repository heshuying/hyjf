package com.hyjf.admin.coupon.config;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class CouponConfigDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/coupon/config";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "coupon/config/config";

	/** 详细画面的路径 新增 */
	public static final String INFO_PATH = "coupon/config/configInfo";
	/** 详细画面的路径 修改*/
	public static final String MODIFY_PATH = "coupon/config/configModify";
	/** 详细画面的路径 审核*/
	public static final String AUDIT_PATH = "coupon/config/configAudit";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "coupon/config/config";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";
	
	/** 更新画面跳转 @RequestMapping值 */
	public static final String UPDATE_INFO_ACTION = "updateInfoAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	
	/** 审核画面跳转的 @RequestMapping值 */
	public static final String AUDIT_ACTION = "auditAction";
	
	/** 审核数据的 @RequestMapping值 */
	public static final String AUDIT_UPDATE_ACTION = "auditUpdateAction";

	/** 校验数据的 @RequestMapping值 */
	public static final String CHECK_ACTION = "checkAction";
	
	/** 数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String FORM = "couponConfigForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "couponconfig";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	
	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 审核权限 */
	public static final String PERMISSIONS_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUDIT;
	
}
