package com.hyjf.admin.manager.config.repaystyle;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RepayStyleDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/borrowstyle";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/borrowstyle/borrowstyle";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/config/borrowstyle/borrowstyleInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/config/borrowstyle/borrowstyle";
	
	/** 重定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/borrowstyle/init";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	
	/** 更新数据的 @RequestMapping值 */
	public static final String STATUS_ACTION = "statusAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String BORROWSTYLE_FORM = "borrowstyleForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "borrowstyle";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
