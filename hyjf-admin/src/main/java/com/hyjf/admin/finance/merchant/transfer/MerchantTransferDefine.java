package com.hyjf.admin.finance.merchant.transfer;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MerchantTransferDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/merchant/transfer";

	/** 用户转账列表 @RequestMapping值 */
	public static final String TRANSFER_LIST_ACTION = "transferList";

	/** 添加用户转账 @RequestMapping值 */
	public static final String INIT_TRANSFER_ACTION = "initTransfer";
	
	/** 用户转账条件校验 @RequestMapping值 */
	public static final String CHECK_TRANSFER_ACTION = "checkTransfer";

	/** 添加用户转账 @RequestMapping值 */
	public static final String ADD_TRANSFER_ACTION = "addTransfer";

	/** 用户转账列表导出 @RequestMapping值 */
	public static final String EXPORT_TRANSFER_ACTION = "exportTransfer";
	
	/** 用户转账同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/returnUrl";
	
	/** 用户转账异步回调 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/bgreturnUrl";

	/** 用户转账列表FROM */
	public static final String TRANSFER_LIST_FORM = "transferListForm";

	/** 用户转账确认FROM */
	public static final String TRANSFER_FORM = "merchanttransferForm";
	
	/** 用户转账列表 */
	public static final String TRANSFER_LIST_PATH = "finance/merchant/transfer/transferList";
	
	/** 用户转账初始化 */
	public static final String INIT_TRANSFER_PATH = "finance/merchant/transfer/transfer";

	/** 权限名称 */
	public static final String PERMISSIONS = "merchanttransferlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 添加权限 */
	public static final String PERMISSION_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 类名 */
	public static final String THIS_CLASS = MerchantTransferController.class.getName();
	
}
