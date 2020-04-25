package com.hyjf.admin.manager.plan.planlock;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanLockDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/planLock";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/planlock/planLockList";

	/** 预览画面路径 */
	public static final String PREVIEW_PATH = "manager/borrow/plan/preview";

	/** 删除后 路径 */
	public static final String INFO_PATH = "manager/borrow/planlock/planLockInfo";
	/** 出借 路径 */
	public static final String INVEST_PATH = "manager/borrow/planlock/planInvest";

	/** 债转购买路径 */
	public static final String CREDIT_TENDER_PATH = "manager/borrow/planlock/plancredittender";
	/** 出借 路径 */
	public static final String INVEST_FALSE_PATH = "manager/borrow/planlock/planInvestFalse";
	/** 出借 路径 */
	public static final String INVEST_SUCCESS_PATH = "manager/borrow/planlock/planInvestSuccess";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 出借画面 @RequestMapping值 */
	public static final String PLANINVEST = "planInvest";
	/** 出借 @RequestMapping值 */
	public static final String INVESTNOW = "investNow";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_SEARCH_ACTION = "infoSearchAction";

	/** 汇添金转让类产品 @RequestMapping值 */
	public static final String CREDIT_INFO_ACTION = "creditInfoAction";

	/** 汇添金转让类产品购买跳转 @RequestMapping值 */
	public static final String CREDIT_TENDER_INFO_ACTION = "creditTenderInfoAction";

	/** 汇添金转让类产品购买 */
	public static final String CREDIT_TENDER_ACTION = "creditTenderAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 预览的 @RequestMapping值 */
	public static final String PREVIEW_ACTION = "previewAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String PLAN_FORM = "planForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "planlock";

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

	/** 预览权限 */
	public static final String PERMISSIONS_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PREVIEW;

}
