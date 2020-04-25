package com.hyjf.bank.service.user.synbalance;

import com.hyjf.bank.service.BaseAjaxResultBean;

public class SynBalanceAjaxBean extends BaseAjaxResultBean {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -8830215900809194471L;
	//同步余额结果
	private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
	
	
}
