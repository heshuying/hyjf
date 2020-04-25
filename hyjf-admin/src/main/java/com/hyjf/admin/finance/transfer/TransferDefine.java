package com.hyjf.admin.finance.transfer;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class TransferDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/transfer";

	/** 用户转账列表 @RequestMapping值 */
	public static final String TRANSFER_LIST_ACTION = "transferList";

	/** 添加用户转账 @RequestMapping值 */
	public static final String INIT_TRANSFER_ACTION = "initTransfer";
	
	/** 用户转账条件校验 @RequestMapping值 */
	public static final String CHECK_TRANSFER_ACTION = "checkTransfer";
	
	/** 用户转账条件校验 @RequestMapping值 */
	public static final String CHECK_AMOUNT_ACTION = "checkAmount";
	
	/** 获取用户余额 @RequestMapping值 */
	public static final String SEARCH_BALANCE_ACTION = "searchBalance";

	/** 添加用户转账 @RequestMapping值 */
	public static final String ADD_TRANSFER_ACTION = "addTransfer";

	/** 用户转账发送邮件 @RequestMapping值 */
	public static final String TRANSFER_SEND_MAIL_ACTION = "transferSendMail";

	/** 用户转账列表导出 @RequestMapping值 */
	public static final String EXPORT_TRANSFER_ACTION = "exportTransfer";

	/** 用户转账列表FROM */
	public static final String TRANSFER_LIST_FORM = "transferListForm";

	/** 用户转账确认FROM */
	public static final String TRANSFER_FORM = "transferForm";
	
	/** 用户转账列表 */
	public static final String TRANSFER_LIST_PATH = "finance/transfer/transferList";
	
	/** 用户转账初始化 */
	public static final String INIT_TRANSFER_PATH = "finance/transfer/transfer";

	/** 权限名称 */
	public static final String PERMISSIONS = "transferlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 添加权限 */
	public static final String PERMISSION_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	
	/** 发送邮件 */
	public static final String PERMISSION_TRANSFER_SEND_EMAIL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_TRANSFER_SEND_EMAIL;
	
	/** 复制链接 */
	public static final String PERMISSION_TRANSFER_COPY_URL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_TRANSFER_COPY_URL;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 类名 */
	public static final String THIS_CLASS = TransferController.class.getName();
	
	/** web工程调用路径*/
	public static final String WEB_TRANSFER_URL = PropUtils.getSystem("hyjf.web.transfer.url").trim();

}
