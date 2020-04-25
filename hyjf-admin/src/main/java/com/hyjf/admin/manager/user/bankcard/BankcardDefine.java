package com.hyjf.admin.manager.user.bankcard;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankcardDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/bankcard";

	/** 用户绑卡明细 */
	public static final String BANK_CARD_LIST_PATH = "manager/users/bankcardlist/bankcardList";

	/** 会员银行卡绑定列表画面 @RequestMapping值 */
	public static final String BANKCARD_LIST_ACTION = "bankcardlist";
	
	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_BANKCARD_ACTION = "exportbankcard";
	
	/** 用户银行卡绑定列表FROM */
	public static final String BANKCARD_LIST_FORM = "bankcardListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "bankcardlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 类名*/
	public static final String THIS_CLASS = BankcardController.class.getName();

}
