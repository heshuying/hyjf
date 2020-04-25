package com.hyjf.api.aems.bankcard;

import com.hyjf.base.bean.BaseResultBean;

public class AemsDeleteBankCardResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 6529261663208896558L;
	
	private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
	
	
	
}
