package com.hyjf.api.surong.user.account;

public class AccountDefine {
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/surong/account";
	/** 获取余额 */
	public static final String GET_BALANCE = "/getBalance";
	/** 获取余银行卡*/
	public static final String GET_BANKCARD = "/getBankCard";
	/** 获取线下充值信息*/
	public static final String OFFLINERECHAGEINFO_ACTION= "/getOfflineRechargeInfo";
	/** 获取账户同步余额 */
	public static final String BALANCE_SYNC = "/balanceSync";
    /** 获取账户同步余额 （新 实时同步江西银行）*/
    public static final String BALANCE_ACTUAL_SYNC = "/balanceActualSync";
}
