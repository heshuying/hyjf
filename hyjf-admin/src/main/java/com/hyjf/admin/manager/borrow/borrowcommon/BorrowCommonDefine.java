package com.hyjf.admin.manager.borrow.borrowcommon;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowCommonDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowcommon";

	/** 借款列表画面 路径 */
	public static final String FIRST_LIST_PATH = "manager/borrow/borrowfirst/borrowfirst";

	/** 全部借款列表画面 路径 */
	public static final String ALL_BORROW_LIST_PATH = "manager/borrow/borrow/borrow";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/borrowcommon/borrowcommon";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 返回 @RequestMapping值 */
	public static final String BACK_ACTION = "backAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 借款主体CAcheck @RequestMapping值 */
	public static final String IS_BORROWUSER_CA_CHECK_ACTION ="isBorrowUserCACheck";

	/** 社会统一信用代码或身份证号CAcheck @RequestMapping值 */
	public static final String IS_CA_IDNO_CHECK_ACTION = "isCAIdNoCheck";

	public static final String DOWNLOAD_CAR_ACTION = "downloadCarAction";
	/** 下载借款内容填充模板 */
	public static final String DOWNLOAD_CONTENT_ACTION = "downloadContentFillAction";

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
	public static final String IS_ACCOUNT_LEGAL_ACTION = "isAccountLegal";

	/** AJAX @RequestMapping值 */
	public static final String ISREPAYORGUSER_ACTION = "isRepayOrgUser";

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSBORROWPRENIDRECORD = "isExistsBorrowPreNidRecord";

	/** AJAX @RequestMapping值 */
	public static final String GETBORROWPRENID_ACTION = "getBorrowPreNid";
	
	/** AJAX @RequestMapping值 */
	public static final String ISEXISTEntrustedUSER_ACTION = "isExistEntrustedUser";

	/**现金贷获取借款预编号 @RequestMapping值 */
	public static final String GETXJDBORROWPRENID_ACTION = "getXJDBorrowPreNid";

	public static final String CONTENT_FILL_ACTION = "contentFillAction";

	/** 获取放款服务费率 & 还款服务费率 AJAX @RequestMapping值 */
	public static final String GETSCALE_ACTION = "getScale";

	public static final String UPLOAD_FILE = "uploadFile";

	/** 获取项目名称和金额 */
	public static final String GET_BORROW_NAME_ACCOUNT_LIST = "getBorrowNameAccountList";

	/** 获取个数 */
	public static final String GET_CUNSUME_COUNT = "getCunsumeCount";
    /**
     * 获取产品类型
     */
    public static final String GET_PRODUCT_TYPE_ACTION = "getProductTypeAction";

	/** FROM */
	public static final String BORROW_FORM = "borrowForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrow";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 借款期限是否为0的check */
	public static final String IS_BORROW_PERIOD_CHECK_ACTION = "isBorrowPeriodCheck";

	public static final String GET_BORROW_LEVEL_ACTION = "getBorrowLevelAction";
	
	/**借款操作日志 操作类型*/
	public static final String BORROW_LOG_UPDATE = "修改";
	public static final String BORROW_LOG_ADD = "新增";
	public static final String BORROW_LOG_DEL = "删除";
}
