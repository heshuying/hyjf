package com.hyjf.admin.manager.debt.debtborrowcommon;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtBorrowCommonDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/debt/debtborrowcommon";

	/** 借款列表画面 路径 */
	public static final String FIRST_LIST_PATH = "manager/debt/debtborrowfirst/debtborrowfirst";

	/** 全部借款列表画面 路径 */
	public static final String ALL_BORROW_LIST_PATH = "manager/debt/debtborrow/debtborrow";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/debt/debtborrowcommon/debtborrowcommon";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 返回 @RequestMapping值 */
	public static final String BACK_ACTION = "backAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	public static final String DOWNLOAD_CAR_ACTION = "downloadCarAction";

	public static final String UPLOAD_CAR_ACTION = "uploadCarAction";

	public static final String DOWNLOAD_HOUSE_ACTION = "downloadHouseAction";

	public static final String UPLOAD_HOUSE_ACTION = "uploadHouseAction";

	public static final String DOWNLOAD_AUTHEN_ACTION = "downloadAuthenAction";

	public static final String UPLOAD_AUTHEN_ACTION = "uploadAuthenAction";

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSUSER_ACTION = "isExistsUser";

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSAPPLICANT_ACTION = "isExistsApplicant";

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSBORROWPRENIDRECORD = "isExistsBorrowPreNidRecord";

	/** AJAX @RequestMapping值 */
	public static final String GETBORROWPRENID_ACTION = "getBorrowPreNid";

	/** 获取放款服务费率 & 还款服务费率 AJAX @RequestMapping值 */
	public static final String GETSCALE_ACTION = "getScale";

	public static final String UPLOAD_FILE = "uploadFile";

	/** 获取项目名称和金额 */
	public static final String GET_BORROW_NAME_ACCOUNT_LIST = "getBorrowNameAccountList";

	/** 获取个数 */
	public static final String GET_CUNSUME_COUNT = "getCunsumeCount";

	/** FROM */
	public static final String BORROW_FORM = "debtborrowForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtborrow";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
