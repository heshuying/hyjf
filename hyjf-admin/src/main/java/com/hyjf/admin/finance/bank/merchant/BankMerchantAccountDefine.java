package com.hyjf.admin.finance.bank.merchant;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankMerchantAccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/merchant/account";

	/** 用户转账列表 @RequestMapping值 */
	public static final String INIT = "init";

	/** 用户转账列表 @RequestMapping值 */
    public static final String SYNBALANCE = "synbalance";
    
    /** 用户转账列表 @RequestMapping值 */
    public static final String WITHDRAWALS_INIT = "withdrawalsInit";
    
    /** 用户转账列表 @RequestMapping值 */
    public static final String WITHDRAWALS = "withdrawals";
    
    /** 用户转账列表 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";
    
    /** 用户红包发放 @RequestMapping值 */
    public static final String INIT_POCKET_SEND_ACTION = "initPocketSendAction";
    
    /** 用户红包发放 @RequestMapping值 */
    public static final String RED_POCKET_SEND_ACTION = "redPocketSendAction";
    
    
    /** @RequestMapping值 */
    public static final String SETPASSWORD_ACTION = "/setPassword";
    /** @RequestMapping值 */
    public static final String RETURL_SYN_PASSWORD_ACTION = "/passwordReturn";
    /** @RequestMapping值 */
    public static final String RETURN_ASY_PASSWORD_ACTION = "/passwordBgreturn";
    
    /** 重置交易密码 */
    public static final String RESETPASSWORD_ACTION = "/resetPassword";
    /** 重置交易密码同步回调 */
    public static final String RETURL_SYN_RESETPASSWORD_ACTION = "/resetPasswordReturn";
    /** 重置交易密码异步回调 */
    public static final String RETURN_ASY_RESETPASSWORD_ACTION = "/resetPasswordBgreturn";
    
    /** 设置密码成功页面*/
    public static final String PASSWORD_SUCCESS_PATH = "/bank/merchant/success";
    /** 设置密码失败页面*/
    public static final String PASSWORD_ERROR_PATH = "/bank/merchant/error";
    
    
	/** 用户转账列表FROM */
	public static final String ACCOUNT_LIST_FORM = "accountListForm";
	
	/** 用户转账列表FROM */
	public static final String RED_POCKET_SEND_FORM = "redPocketSendForm";
	
	/** 用户转账列表 */
	public static final String ACCOUNT_LIST_PATH = "bank/merchant/merchantaccountlist";
	
	/** 用户转账列表 */
    public static final String WITHDRAWALS_INIT_PATH = "bank/merchant/withdrawalsInfo";
    
    /** 提现页面返回成功 */
    public static final String WITHDRAW_SUCCESS = "/bank/merchant/withdraw_success";
    /** 提现页面 返回失败 */
    public static final String WITHDRAW_FALSE = "/bank/merchant/withdraw_false";
    /** 提现页面 提现前的信息提示 */
    public static final String WITHDRAW_INFO = "/bank/merchant/withdraw_info";
    
    /** 用户转账列表 */
    public static final String RED_POCKET_SEND_PATH = "bank/merchant/redpocketsend";
	
	/** 权限名称 */
	public static final String PERMISSIONS = "bankmerchantaccount";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 添加权限 */
	public static final String PERMISSION_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_REDPOCKETSEND = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_REDPOCKETSEND;

	/** 类名 */
	public static final String THIS_CLASS = BankMerchantAccountController.class.getName();
	
	
	/** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";
    
    //-------------------------------------------圈存 开始--------------------------------------------------
    /** 圈存 @RequestMapping值 */
    public static final String RECHARGE_INIT = "rechargeInit";
    
    /**  圈存页面路径  */
    public static final String RECHARGE_INIT_PATH = "bank/merchant/rechargeInfo";
    
    /**
     * 调用圈存接口
     */
    public static final String TO_RECHARGE = "toRecharge";
    
    /** 圈存页面返回成功 */
    public static final String RECHARGE_SUCCESS = "/bank/merchant/recharge_success";
    /** 圈存页面 返回失败 */
    public static final String RECHARGE_FALSE = "/bank/merchant/recharge_false";
    /** 圈存页面 提现前的信息提示 */
    public static final String RECHARGE_INFO = "/bank/merchant/recharge_info";
    
    /** 圈存同步@RequestMapping值 */
    public static final String RECHARGE_RETURN_MAPPING = "/rechargeReturn";
    /** 圈存异步@RequestMapping值 */
    public static final String RECHARGE_CALLBACK_MAPPING = "/rechargeCallback";
    
    

}
