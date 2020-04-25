package com.hyjf.admin.manager.plan.release;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanReleaseDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/planrelease";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "/manager/borrow/planrelease/planreleaseList";

	/** 提审画面 路径 */
	public static final String ARRAIGNMENT_PATH = "/manager/borrow/planrelease/arraignmentInfo";

	/** 审核画面 路径 */
	public static final String AUDIT_PATH = "/manager/borrow/planrelease/planreleaseInfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 提审画面 @RequestMapping值 */
	public static final String ARRAIGNMENT_ACTION = "arraignmentAction";

	/** 确认提审 @RequestMapping值 */
	public static final String ARRAIGNMENT_OK_ACTION = "arraignmentOkAction";

	/** 计划审核 @RequestMapping值 */
	public static final String AUDIT_PLAN_ACTION = "auditPlanAction";

	/** 审核后更新计划详情 @RequestMapping值 */
	public static final String UPDATE_PLAN_ACTION = "updatePlanAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 预览的 @RequestMapping值 */
	public static final String PREVIEW_ACTION = "previewAction";
	
	/**关联资产检索Action @RequestMapping*/
	public static final String SEARCH_INFO_ACTION = "searchInfoAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String PLAN_FORM = "planForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "planrelease";

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

	/** 提审权限 */
	public static final String PERMISSION_TISHEN = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_TISHEN;

	/** 审核权限 */
	public static final String PERMISSION_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUDIT;
}
