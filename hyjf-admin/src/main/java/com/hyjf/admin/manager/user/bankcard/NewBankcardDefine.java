package com.hyjf.admin.manager.user.bankcard;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class NewBankcardDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/newbankcard";

	
	/*****************************************银行存管 银行卡管理  pcc ******************************************/
	/** 用户绑卡明细 */
    public static final String NEW_BANK_CARD_LIST_PATH = "manager/users/bankcardlist/newbankcardList";

    /** 会员银行卡绑定列表画面 @RequestMapping值 */
    public static final String NEW_BANKCARD_LIST_ACTION = "bankcardlist";
    
    /** 获取会员管理列表 @RequestMapping值 */
    public static final String EXPORT_NEW_BANKCARD_ACTION = "exportnewbankcard";
    /*****************************************银行存管 银行卡管理  pcc ******************************************/

	/** 用户银行卡绑定列表FROM */
	public static final String BANKCARD_LIST_FORM = "bankcardListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "bankcardlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 类名*/
	public static final String THIS_CLASS = BankcardController.class.getName();

}
