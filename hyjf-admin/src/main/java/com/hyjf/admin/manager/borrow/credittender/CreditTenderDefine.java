package com.hyjf.admin.manager.borrow.credittender;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class CreditTenderDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/credittender";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/credittender/credittender";

	/** PDF签署Action @RequestMapping值*/
	public static final String PDF_SIGN_ACTION = "pdfSignAction";

	/** PDF脱敏图片预览Action @RequestMapping值 */
	public static final String PDF_PREVIEW_ACTION = "pdfPreviewAction";

	/** PDF脱敏图片预览路径 @RequestMapping值 */
	public static final String PDF_PREVIEW_PATH  = "manager/borrow/borrowinvest/pdfpreview";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 获取列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

	/** 出借人投标查询 @RequestMapping */
	public static final String QUERY_INVEST_DEBT_ACTION = "queryInvestDebtAction";

	/** 出借人投标查询路径 @RequestMapping */
	public static final String QUERY_INVEST_DEBT_PATH = "manager/borrow/credittender/investorinfo";

	/** FROM */
	public static final String FORM = "creditTenderForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "credittender";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

	/** 法大大PDF脱敏图片预览权限 */
	public static final String PERMISSIONS_PDF_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_PREVIEW;

	/** 法大大PDF签署权限 */
	public static final String PERMISSIONS_PDF_SIGN = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_SIGN;

	/** 单笔查询出借人投标申请 */
	public static final String PERMISSIONS_QUERY_INVEST_DEBT_VIEW =  PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_QUERY_INVEST_DEBT_VIEW;
}
