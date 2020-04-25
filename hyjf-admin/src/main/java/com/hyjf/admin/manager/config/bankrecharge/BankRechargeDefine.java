package com.hyjf.admin.manager.config.bankrecharge;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankRechargeDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/bankrecharge";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/bankrecharge/bankrecharge";
	
	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/bankrecharge/init";
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/config/bankrecharge/bankrechargeInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/config/bankrecharge/bankrecharge";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String CHECK_ACTION = "checkAction";
	
	/** 汇付查询配置 @RequestMapping值 */
	public static final String QUERY_ACTION = "queryAction";
	
	/** FROM */
	public static final String BANKRECHARGE_FORM = "bankrechargeForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "bankrecharge";
	
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

}
