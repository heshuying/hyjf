package com.hyjf.admin.manager.plan.plancommon;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanCommonDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/plancommon";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/plancommon/plancommon";

	/** 详细画面的路径 */
	public static final String DETAIL_PATH = "manager/borrow/plancommon/plancommonInfo";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 详情画面 @RequestMapping值 */
	public static final String DETAIL_ACTION = "detailAction";

	/** 资产关联列表查询 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 详情关联资产的查询 @RequestMapping值 */
	public static final String SEARCH_INFO_ACTION = "searchInfoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 返回 @RequestMapping值 */
	public static final String BACK_ACTION = "backAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 获取计划名称 @RequestMapping */
	public static final String GET_PLAN_NAME_ACTION = "getPlanName";
	
	
	/** AJAX @RequestMapping值 */
	public static final String GETPLANPRENID_ACTION = "getPlanPreNid";

	/** 判断计划名称是否存在 @RequestMapping */
	public static final String ISDEBTPLANNAMEEXIST_ACTION = "isDebtPlanNameExist";
	
	/**判断计划预编号是否存在 @RequestMapping*/
	public static final String ISEXISTSPLANPRENID_ACTION = "isExistsPlanPreNidRecord";

	/** Form */
	public static final String PLAN_FORM = "planForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "planmanager";

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 查看权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
