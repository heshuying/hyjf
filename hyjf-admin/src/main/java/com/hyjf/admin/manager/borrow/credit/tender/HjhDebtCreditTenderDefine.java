package com.hyjf.admin.manager.borrow.credit.tender;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HjhDebtCreditTenderDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String CONTROLLER_NAME = HjhDebtCreditTenderController.class.getName();

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/hjhcredit/hjhcredittender";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/hjhcredit/hjhcredittender/hjhcredittender";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** PDF签署Action @RequestMapping值*/
	public static final String PDF_SIGN_ACTION = "pdfSignAction";

	/** 运营记录-承接明细 */
	public static final String OPT_ACTION_INIT = "optActionInit";

	/** 运营记录-承接明细 */
	public static final String OPT_ACTION_SEARCH = "optActionSearch";

	/** PDF脱敏图片预览Action @RequestMapping值 */
	public static final String PDF_PREVIEW_ACTION = "pdfPreviewAction";

	/** PDF脱敏图片预览路径 @RequestMapping值 */
	public static final String PDF_PREVIEW_PATH  = "manager/borrow/borrowinvest/pdfpreview";
	/** FROM */
	public static final String FORM = "hjhcredittenderForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "hjhcredittender";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 法大大PDF脱敏图片预览权限 */
	public static final String PERMISSIONS_PDF_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_PREVIEW;

	/** 法大大PDF签署权限 */
	public static final String PERMISSIONS_PDF_SIGN = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_SIGN;

}
