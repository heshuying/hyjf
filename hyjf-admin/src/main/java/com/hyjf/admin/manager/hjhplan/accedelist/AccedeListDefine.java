package com.hyjf.admin.manager.hjhplan.accedelist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccedeListDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/hjhplan/accedelist";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 加入明细列表 @RequestMapping */
	public static final String LIST_PATH = "/manager/hjhaccede/joindetailList";

	/** 检索画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";

	/** 导出列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

	/** PDF签署Action @RequestMapping值*/
	public static final String PDF_SIGN_ACTION = "pdfSignAction";

	/** PDF脱敏图片预览Action @RequestMapping值 */
	public static final String PDF_PREVIEW_ACTION = "pdfPreviewAction";

	/** PDF脱敏图片预览路径 @RequestMapping值 */
	public static final String PDF_PREVIEW_PATH  = "manager/borrow/borrowinvest/pdfpreview";
	
	/** FORM */
	public static final String JOINDETAIL_FORM = "accedeForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "accedelist";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;
	
	/** 跳转到发送协议请求  @RequestMapping值 */
    public static final String TOEXPORT_AGREEMENT_ACTION = "toExportAgreementAction";
    
	/** 发送协议入力email画面 路径 */
    public static final String EXPORT_AGREEMENT_PATH = "/manager/hjhaccede/exportagreement";
    
   	/** 发送协议email入力后请求 @RequestMapping值 */
    public static final String EXPORT_AGREEMENT_ACTION = "exportAgreementAction";
    
	/** 出借明细请求 @RequestMapping值 */
	public static final String TENDER_INFO = "tenderInfoAction";
	
	/** 出借明细列表画面 */
	public static final String TENDER_INFO_LIST_PATH = "/manager/hjhaccede/tenderdetailList";

	/** FORM */
	public static final String TENDER_INFO_FORM = "tenderForm"; 
	
	/** 跳转到出借协议重发请求  @RequestMapping值 */
    public static final String RESEND_MESSAGE_ACTION = "resendMessageAction";

	/** 法大大PDF脱敏图片预览权限 */
	public static final String PERMISSIONS_PDF_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_PREVIEW;

	/** 法大大PDF签署权限 */
	public static final String PERMISSIONS_PDF_SIGN = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PDF_SIGN;
	
}
