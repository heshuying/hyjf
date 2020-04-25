package com.hyjf.admin.manager.user.appoint;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AppointDefine extends BaseDefine {

	/** 配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/users/appoint";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "/manager/users/appoint/appoint_list";
	/** 列表画面 路径 */
	public static final String AUTH_LIST_PATH = "/manager/users/appoint/appoint_list_auth";
	/** 列表画面 路径 */
	public static final String APPOINT_LIST_MAIN_PATH = "/manager/users/appoint/appoint_list_record_main";
	/** 列表画面 路径 */
	public static final String APPOINT_LIST_INFO_PATH = "/manager/users/appoint/appoint_list_record_info";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "app/maintenance/borrowimage/borrowimage_info";


	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面 @RequestMapping值 */
	public static final String AUTHLIST = "authList";
	/** 列表画面 @RequestMapping值 */
	public static final String APPOINTLISTRECORDMAIN = "appointlistrecordmain";
	/** 列表画面 @RequestMapping值 */
	public static final String APPOINTLISTRECORD = "appointlistrecord";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** 检索数据 @RequestMapping值 */
	public static final String AUTH_SEARCH_ACTION = "authlistSearchAction";
	/** 检索数据 @RequestMapping值 */
	public static final String APPOINT_MAIN_SEARCH_ACTION = "appointlistrecordmainSearchAction";
	/** 检索数据 @RequestMapping值 */
	public static final String APPOINT_RECORD_SEARCH_ACTION = "appointlistrecordSearchAction";

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
	public static final String PERMISSIONS = "appoint";

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
