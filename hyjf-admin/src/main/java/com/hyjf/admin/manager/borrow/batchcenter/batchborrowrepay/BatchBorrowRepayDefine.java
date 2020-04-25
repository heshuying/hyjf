package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrepay;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BatchBorrowRepayDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/batchcenter/batchborrowrepay";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/batchcenter/borrowrepay/batchborrowrepay";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** FROM */
	public static final String BORROW_FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "batchborrowrepay";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	public static final String QUERY_BATCH_DETAILS_ACTION = "queryBatchDetailClkAction";

	public static final String PERMISSIONS_QUERY_BATCH_DETAILS = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_QUERY_BATCHDETAIL;

	public static final String QUERY_BATCH_DETAILS_PATH = "manager/borrow/batchcenter/borrowrepay/queryBatchDetails";


}
