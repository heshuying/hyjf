package com.hyjf.admin.finance.bankaccountmanage;

public class BankAccountCheckConstants {

	public static final int TYPE_ID_PAY = 1;//支出
	public static final int TYPE_ID_INCOME = 3;//收入
	// 充值状态:失败
	public static final int RECHARGE_STATUS_FAIL = 0;
	// 充值状态:成功
	public static final int RECHARGE_STATUS_SUCCESS = 1;
	// 充值状态:充值中
	public static final int RECHARGE_STATUS_WAIT = 2;
	//入账状态:已对账
	public static final int CHECK_STATUS_SUCCESS = 1;
	//入账状态:未入账
	public static final int CHECK_STATUS_FAIL = 0;
	//交易状态
	public static final int TRADE_STATUS_FAIL = 0;//失败
	public static final int TRADE_STATUS_SUCCESS = 1;//成功
	public static final int TRADE_STATUS_RETURN = 2;//冲正
	
}
