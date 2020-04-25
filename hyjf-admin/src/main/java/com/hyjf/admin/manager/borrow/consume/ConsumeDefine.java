package com.hyjf.admin.manager.borrow.consume;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ConsumeDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/consume";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/consume/consume";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 返回 @RequestMapping值 */
	public static final String BACK_ACTION = "backAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSUSER_ACTION = "isExistsUser";

	/** AJAX @RequestMapping值 */
	public static final String GETBORROWPRENID_ACTION = "getBorrowPreNid";

	/** 获取放款服务费率 & 还款服务费率 AJAX @RequestMapping值 */
	public static final String GETSCALE_ACTION = "getScale";

	public static final String UPLOAD_FILE = "uploadFile";

	/** FROM */
	public static final String BORROW_FORM = "consumeForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "consume";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
