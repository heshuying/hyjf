package com.hyjf.mqreceiver.hgdatareport.cert.transact;

public enum TradeTypeEnum {
	//提现
	CASH_SUCCESS("7", "cash_success"),
	//充值
	RECHARGE("6", "recharge"),
	//线下充值
	RECHARGE_OFFLINE("6", "recharge_offline"),
	//借款成功
	BORROW_SUCCESS("1", "borrow_success"),
	//还款
	REPAY_SUCCESS("18", "repay_success"),
	
	//散标投资
	TENDER("2", "tender"),
	//优惠券回款
	EXPERIENCE_PROFIT("10", "experience_profit"),
	//优惠券回款
	INCREASE_INTEREST_PROFIT("10", "increase_interest_profit"),
	//优惠券回款
	CASH_COUPON_PROFIT("10", "cash_coupon_profit"),
	//投资成功-汇计划
	HJH_TENDER_SUCCESS("2", "hjh_tender_success"),
	//投资成功-汇计划
	TENDER_SUCCESS("2", "tender_success"),
	//投资收到还款
	TENDER_RECOVER_YES("8", "tender_recover_yes"),
	//承接债权
	CREDITASSIGN("17", "creditassign"),
	//自动承接债权
	ACCEDE_ASSIGN("17", "accede_assign"),
	//智投清算（转让）
	LIQUIDATES_SELL("11", "liquidates_sell"),
	//出让债权
	CREDITSELL("11", "creditsell"),
	
	
	//债转还款
	CREDIT_TENDER_RECOVER_YES("8", "credit_tender_recover_yes");
	
	
	
	
		
	
	
	/*//活动奖励
	BORROWACTIVITY("红色", "borrowactivity"),
	//vip购买
	APPLY_VIP("红色", "apply_vip"),
	//汇计划投资
	HJH_INVEST("红色", "hjh_invest"),
	//存管收益
	ACCOUNT_INTEREST("红色", "account_interest"),
	//自动冲正
	AUTO_REVERSE("红色", "auto_reverse"),
	//收到还款冻结
	HJH_REPAY_FROST("红色", "hjh_repay_frost"),
	//收到还款复投
	HJH_REPAY_BALANCE("红色", "hjh_repay_balance"),
	//退出计划
	HJH_QUIT("红色", "hjh_quit"),
	//智投订单锁定
	HJH_LOCK("红色", "hjh_lock"),
	//专属项目回款
	PLAN_BORROW_REPAY("红色", "plan_borrow_repay"),
	//定向转账(支出)
	DIRECTIONAL_TRANSFER_PAY("红色", "directional_transfer_pay"),
	//用户转账
	USER_TRANSFER("红色", "user_transfer");  */
	

    // 成员变量  
    private String name;  
    private String index;  
    // 构造方法  
    private TradeTypeEnum(String name, String index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(String index) {  
        for (TradeTypeEnum c : TradeTypeEnum.values()) {  
            if (c.getIndex().equals(index)) {  
                return c.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public String getIndex() {  
        return index;  
    }  
    public void setIndex(String index) {  
        this.index = index;  
    }  
}