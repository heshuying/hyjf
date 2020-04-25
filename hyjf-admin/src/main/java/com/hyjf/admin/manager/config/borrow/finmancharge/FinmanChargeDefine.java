package com.hyjf.admin.manager.config.borrow.finmancharge;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class FinmanChargeDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/finmancharge";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/borrow/finmancharge/finmancharge";

	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/borrow/finmancharge/init";

	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/config/borrow/finmancharge/finmanchargeInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/config/borrow/finmancharge/finmancharge";

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

	/** AJAX 验证 */
	public static final String CHECK_ACTION = "checkAction";

	/** FROM */
	public static final String FINMANCONFIGFORM = "finmanchargeForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "finmancharge";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
