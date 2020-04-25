package com.hyjf.admin.exception.autotenderexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AutoTenderExceptionDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/autotenderexception";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 加入明细列表 @RequestMapping */
	public static final String LIST_PATH = "/exception/autotenderexception/autotenderexception";

	/** 检索画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
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
	
	/** 异常处理权限 */
	public static final String PERMISSIONS_AUTOTENDEREXCEPTION = PERMISSIONS + StringPool.COLON + "AUTOTENDEREXCEPTION";
	
	/** 跳转到发送协议请求  @RequestMapping值 */
    public static final String TOEXPORT_AGREEMENT_ACTION = "toExportAgreementAction";
    
	/** 发送协议入力email画面 路径 */
    public static final String EXPORT_AGREEMENT_PATH = "/exception/autotenderexception/exportagreement";
    
   	/** 发送协议email入力后请求 @RequestMapping值 */
    public static final String EXPORT_AGREEMENT_ACTION = "exportAgreementAction";
    
    public static final String TENDER_EXCEPTION_ACTION = "tenderExceptionAction";
    
	/** 出借明细请求 @RequestMapping值 */
	public static final String TENDER_INFO = "tenderInfoAction";
	
	/** 出借明细列表画面 */
	public static final String TENDER_INFO_LIST_PATH = "/exception/autotenderexception/tenderdetailList";

	/** FORM */
	public static final String TENDER_INFO_FORM = "tenderForm"; 
	
	/** 跳转到出借协议重发请求  @RequestMapping值 */
    public static final String RESEND_MESSAGE_ACTION = "resendMessageAction";
	
}
