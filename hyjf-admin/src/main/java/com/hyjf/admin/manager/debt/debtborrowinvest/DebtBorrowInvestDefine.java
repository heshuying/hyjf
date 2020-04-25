package com.hyjf.admin.manager.debt.debtborrowinvest;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtBorrowInvestDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/debt/debtborrowinvest";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/debt/debtborrowinvest/debtborrowinvest";

	/** 导出协议画面 路径 */
	public static final String EXPORT_AGREEMENT_PATH = "manager/debt/debtborrowinvest/debtexportagreement";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 导出数据 @RequestMapping值 */
    public static final String RESEND_MESSAGE_ACTION = "resendMessageAction";
    
	/** 导出协议 @RequestMapping值 */
    public static final String EXPORT_AGREEMENT_ACTION = "exportAgreementAction";
    
	/** 跳转到导出协议 @RequestMapping值 */
    public static final String TOEXPORT_AGREEMENT_ACTION = "toExportAgreementAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_FORM = "debtBorrowInvestForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtborrowinvest";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 发送协议权限 */
	public static final String PERMISSIONS_EXPORT_AGREEMENT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT_AGREEMENT;

}
