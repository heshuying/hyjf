package com.hyjf.admin.wkcd.wkcdborrow;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class WkcdBorrowDefine extends BaseDefine {
	/** 预注册Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/manager/wkcdBorrow";

	/** 预注册信息列表@RequestMapping值 */
	public static final String WKCDBORROW_LIST_ACTION = "init";

	/** 跳至预注册信息页面 */
	public static final String GOTO_LIST = "/manager/wkcd/wkcdborrow/wkcdborrow";
	
	/** 跳转至详情和审核页面  */
	public static final String GOTO_DETAIL = "/manager/wkcd/wkcdborrow/detail";

	/** 类名 */
	public static final String THIS_CLASS = WkcdBorrowController.class.getName();

	/** 预注册FORM */
	public static final String WKCDBORROW_FORM = "wkcdBorrowForm";

	/** 导出Excel */
	public static final String EXPORT = "export";
	
	/** 详情&审核 */
	public static final String DETAIL = "detail";
	
	/** 审核提交 */
	public static final String VERIFY = "verify";
	
	/** 获取微可车贷Token */
	public static final String GETTOKEN = "getToken";
	
	/** 跳转至微可详情页 */
	public static final String GOTO_WKCD_PAGE = "gotoWkcdPage";
	
	/** 下载附件 */
	public static final String DOWNLOAD = "download";

	/** 权限 */
	public static final String PERMISSIONS = "wkcdBorrow";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
}
