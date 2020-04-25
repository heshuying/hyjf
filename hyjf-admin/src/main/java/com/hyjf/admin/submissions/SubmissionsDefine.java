package com.hyjf.admin.submissions;

import com.hyjf.admin.BaseDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersController;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SubmissionsDefine extends BaseDefine {
	
	
	/** 列表画面 @RequestMapping值 */
	
	
	public static final String SUBMISSIONS="/submissions/submissions";

	/** 画面初始化请求 @RequestMapping值 */
	public static final String QUERY_SUBMISSIONS_ACTION="querySubmissionsAction";
	/** 画面编辑按钮 @RequestMapping值 */
	public static final String EDIT_SUBMISSIONS_ACTION="editSubmissionsAction";
	/** 画面保存按钮 @RequestMapping值 */
	public static final String UPDATE_SUBMISSIONS_ACTION="updateSubmissionsAction";
	/** 列表导出按钮 @RequestMapping值 */
	public static final String EXPORT_LIST_ACTION="exportListAction";
	/** 意见反馈列表画面URL */
	public static final String SUBMISSIONS_PATH="submissions/submissions";
	/** 意见反馈处理画面URL */
	public static final String SUBMISSIONS_REPLY_PATH="submissions/replySubmissions";
	/** 意见反馈处理画面关闭flg */
	public static final String SUCCESS = "success";
	
	/** 意见反馈列表画面FORM */
	public static final String SUBMISSIONS_LIST_FORM = "submissionsListForm";
	
	/** 类名 */
	public static final String THIS_CLASS = ManageUsersController.class.getName();
	

	/** 权限关键字 */
	public static final String PERMISSIONS = "submissions";
	
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
