package com.hyjf.admin.manager.config.htjconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HtjConfigDefine extends BaseDefine {

	/** 汇添金配置 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/htjconfig";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/htjconfig/htjconfigList";
	
	/** 重定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/htjconfig/init";

	/** 列表画面迁移 */
	public static final String INFO_PATH = "manager/config/htjconfig/htjconfigInfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/**检索Action @RequestMapping*/
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 数据的校验 @RequestMapping值 */
	public static final String CHECK_ACTION = "checkAction";

	/** FROM */
	public static final String HTJCONFIG_FORM = "htjconfigForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "htjconfig";

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
}
