package com.hyjf.admin.exception.hjhcreditendexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HjhCreditEndExceptionDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/hjhcreditendexception";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 加入明细列表 @RequestMapping */
	public static final String LIST_PATH = "/exception/bankdebtend/hjhcreditend";

	/** 检索画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** FORM */
	public static final String JOINDETAIL_FORM = "hjhDebtCreditForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "hjhcreditend";
	
	/** 查看权限 @RequstMapping */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 修改权限 @RequestMapping */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;
	
	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 异常处理权限 */
	public static final String PERMISSIONS_HJHCREDITENDEXCEPTION = PERMISSIONS + StringPool.COLON + "HJHCREDITENDEXCEPTION";
    
    public static final String TENDER_EXCEPTION_ACTION = "tenderExceptionAction";
    
	/** 更新 @RequestMapping值 */
	public static final String MODIFY_ACTION = "modifyAction";
	
	/** 出借明细列表画面 */
	public static final String TENDER_INFO_LIST_PATH = "/exception/hjhcreditendexception/tenderdetailList";

	/** FORM */
	public static final String TENDER_INFO_FORM = "tenderForm"; 
	
	/** 跳转到出借协议重发请求  @RequestMapping值 */
    public static final String RESEND_MESSAGE_ACTION = "resendMessageAction";
	
}
