package com.hyjf.admin.app.maintenance.borrowimage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AppBorrowImageDefine extends BaseDefine {

	/** 配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/app/maintenance/borrowimage";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "app/maintenance/borrowimage/borrowimage";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "app/maintenance/borrowimage/borrowimage_info";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "app/maintenance/borrowimage/borrowimage";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** AJAX 验证 */
	public static final String CHECK_ACTION = "checkAction";

	public static final String UPLOAD_FILE = "uploadFile";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrow_image";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

}
