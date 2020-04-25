package com.hyjf.admin.exception.transferexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 转账异常模块定义的常量
 */
public class TransferExceptionLogDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/transferexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "/exception/transferexception/transferExceptionList";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/**转账确认 @RequestMapping值*/
	public static final String CONFIRM_ACTION = "confirmAction";
	
	/**重新执行转账的 @RequestMapping值*/
    public static final String TRANSFER_AGAIN_ACTION = "transferAgainAction";
	
	/** FROM */
	public static final String FORM = "transferExceptionForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "transferexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;
    
    /** 修复权限 */
    public static final String PERMISSIONS_RECOVER = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_TRANSFER_EXCEPTION;


}
