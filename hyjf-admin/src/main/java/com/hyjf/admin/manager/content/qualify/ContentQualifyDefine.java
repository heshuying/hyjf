package com.hyjf.admin.manager.content.qualify;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ContentQualifyDefine extends BaseDefine {

	/** 配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/content/contentqualify";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/content/contentqualify/contentqualify";

	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/content/contentqualify/init";
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/content/contentqualify/contentqualifyInfo";

	/** 从定向 路径 */
	public static final String FO_LIST_PATH = "forward:/manager/content/contentqualify/init";
	
	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/content/contentqualify/contentqualify";

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
	
	/** 更新数据 @RequestMapping值 */
	public static final String STATUS_ACTION = "statusAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	public static final String UPLOAD_FILE = "uploadFile";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String CONTENTQUALIFY_FORM = "contentqualifyForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "contentqualify";
	
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