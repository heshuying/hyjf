package com.hyjf.admin.manager.allocationengine;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 标的分配规则引擎
 * 
 * @author
 * 
 */
public class AllocationEngineDefine extends BaseDefine {
	
	/** 计划专区列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/allocationengine/planlist";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/allocationengine/allocationengine";/*manager/content/contentlinks/contentlinks*/
	
	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/allocationengine/planlist/init";
	
	/** 从定向 路径 */
	public static final String RE_PLAN_LIST_PATH = "redirect:/manager/allocationengine/planlist/planConfig";
	
	/** 迁移详情 路径 */
	public static final String INFO_PATH = "manager/allocationengine/allocationengineInfo";
	
	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	
	/** 迁移到计划配置详细画面 @RequestMapping值 */
	public static final String INFO_CONFIG_ACTION = "infoConfigAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	
	/** 更新数据 @RequestMapping值 */
	public static final String STATUS_ACTION = "statusAction";
	
	/** 更新数据 @RequestMapping值 */
	public static final String LABEL_STATUS_ACTION = "labelStatusAction";
	
	/** 表单FROM */  
	public static final String ALLOCATIONENGINE_FORM = "allocationengineForm";/*contentlinksForm*/
	
	/** 表单FROM */  
	public static final String GINE_FORM = "allocationengineForm";/*contentlinksForm*/

	/** 权限关键字 */
	public static final String PERMISSIONS = "allocationengine";/*contentlinks*/
	
	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** exeportExecl */
	public static final String EXPORTEXECLPLANCONFIG = "exportPlanConfigExcel";
	
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

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
	
	/** AJAX @RequestMapping值 */
	public static final String CHECKINPUTPLANNIDSRCH = "checkInputPlanNidSrch";//planNidSrch
	
	/** 计划配置请求 @RequestMapping值 */
	public static final String PLANCONFIG = "planConfig";
	
	/** 列表画面 */
	public static final String PLANCONFIG_LIST_PATH = "manager/allocationengine/planconfig";
	
	/** 迁移详情 路径 */
	public static final String INFO_CONFIG_PATH = "manager/allocationengine/planconfigInfo";
	
	/** 迁移详情 路径 */
	public static final String CHANGE_CONFIG_PATH = "manager/allocationengine/planconfigChange";

	/** AJAX @RequestMapping值 */
	public static final String CHECKINPUTLABELNAME = "checkInputlabelname";//labelName
	
	/** AJAX @RequestMapping值 */
	public static final String CHECKINPUTLABELSORT = "checkInputlabelSort";//labelSort
	
	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_CONFIG_ACTION = "insertConfigAction";
	
	/** 更新数据 @RequestMapping值 */
	public static final String MODIFY_ACTION = "modifyAction";
	
	/** 换绑 @RequestMapping值 */
	public static final String CHANGE_ACTION = "changeAction";

	/** 换绑 @RequestMapping值 */
	public static final String CHANGE_ACTION_INFO = "changeActionInfo";
	
	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_CONFIG_ACTION = "updateConfigAction";
	
	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_CONFIG_ACTION_INFO = "updateConfigActionInfo";
	
	/** 更新前报消息 @RequestMapping值 */
	public static final String REPORT_ACTION = "reportAction";
	
	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSPLANNUMBER_ACTION = "isExistsPlaneNumber";
}
