package com.hyjf.admin.maintenance.paramname;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ParamNameDefine extends BaseDefine {

	/**
	 * CONTROLLOR的REQUEST_MAPPING
	 */
	public static final String REQUEST_MAPPING = "/maintenance/paramname";

	/**
	 * 列表画面的路径
	 */
	public static final String LIST_PATH = "maintenance/paramname/paramname_list";

	/**
	 * 详细画面的路径
	 */
	public static final String INFO_PATH = "maintenance/paramname/paramname_info";

	/**
	 * 删除后画面跳转的路径
	 */
	public static final String DELETE_REDIRECT_PATH = "redirect:/maintenance/paramname/init";

	/**
	 * 列表画面初始化
	 */
	public static final String INIT = "init";

	/**
	 * 列表检索
	 */
	public static final String SEARCH_ACTION = "searchAction";

	/**
	 * 从列表画面迁移到详细画面
	 */
	public static final String INFO_ACTION = "infoAction";

	/**
	 * 数据插入Action
	 */
	public static final String INSERT_ACTION = "insertAction";

	/**
	 * 数据插入Action
	 */
	public static final String UPDATE_ACTION = "updateAction";

	/**
	 * 数据删除Action
	 */
	public static final String DELETE_ACTION = "deleteAction";

	/**
	 * AJAX验证Action
	 */
	public static final String AJAX_CHECK_ACTION = "ajaxCheckAction";

	/**
	 * 画面FORM内容
	 */
	public static final String FORM = "formInfo";

	/**
	 * 画面控制权限，对应菜单的ctrl
	 */
	public static final String PERMISSIONS = "paramname";

	/**
	 * 列表查看权限
	 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/**
	 * 列表查询权限
	 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/**
	 * 详细画面权限
	 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/**
	 * 插入权限
	 */
	public static final String PERMISSIONS_INSERT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/**
	 * 更新权限
	 */
	public static final String PERMISSIONS_UPDATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/**
	 * 删除权限
	 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

}
