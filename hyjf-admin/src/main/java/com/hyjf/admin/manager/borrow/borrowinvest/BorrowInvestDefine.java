package com.hyjf.admin.manager.borrow.borrowinvest;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowInvestDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowinvest";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowinvest/borrowinvest";

	/** 导出协议画面 路径 */
	public static final String EXPORT_AGREEMENT_PATH = "manager/borrow/borrowinvest/exportagreement";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 导出数据 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_USERS_ACTION = "enhanceExportAction";
	
	/** 导出数据 @RequestMapping值 */
    public static final String RESEND_MESSAGE_ACTION = "resendMessageAction";
    
	/** 导出协议 @RequestMapping值 */
    public static final String EXPORT_AGREEMENT_ACTION = "exportAgreementAction";
    
	/** 跳转到导出协议 @RequestMapping值 */
    public static final String TOEXPORT_AGREEMENT_ACTION = "toExportAgreementAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** PDF签署Action @RequestMapping值*/
	public static final String PDF_SIGN_ACTION = "pdfSignAction";

	/** PDF脱敏图片预览Action @RequestMapping值 */
	public static final String PDF_PREVIEW_ACTION = "pdfPreviewAction";

	/** PDF脱敏图片预览路径 @RequestMapping值 */
	public static final String PDF_PREVIEW_PATH  = "manager/borrow/borrowinvest/pdfpreview";

	/** FROM */
	public static final String BORROW_FORM = "borrowInvestForm";

	/** 运营记录-出借明细 */
	public static final String OPT_ACTION_INIT = "optActionInit";

	/** 运营记录-出借明细 */
	public static final String OPT_ACTION_SEARCH = "optActionSearch";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowinvest";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

	/** 发送协议权限 */
	public static final String PERMISSIONS_EXPORT_AGREEMENT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT_AGREEMENT;

	/** 法大大PDF签署权限 */
	public static final String PERMISSIONS_PDF_SIGN = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_SIGN;
	
	/** 出借人债权明细(弹窗)Action */
	public static final String DEBT_CHECK_ACTION = "debtCheckAction";
	
	/** 出借人债权明细查看权限 */
	public static final String PERMISSIONS_DEBTCHECK = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DEBTCHECK;

	/** 法大大PDF脱敏图片预览权限 */
	public static final String PERMISSIONS_PDF_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_PREVIEW;
	
	/** FORM */
	public static final String DEBT_CHECK_FORM = "debtCheckForm";
	
	/** 出借人债权明细(弹窗)页面 */
	public static final String BANK_ACCOUNT_CHECK_PATH = "manager/borrow/borrowinvest/bankaccountcheck";
	
	/** 出借人债权明细(弹窗)Action */
	public static final String DEBT_CHECK_INFO_ACTION = "debtCheckInfoAction";
	
	/** 输出FROM */
	public static final String BORROW_NID = "borrowNid";
	
	/** 出借人债权明细列表画面 路径 */
	public static final String INVESTOR_INFO = "manager/borrow/borrowinvest/investorinfo";

	
}
