package com.hyjf.api.aems.bankcard;

import com.hyjf.base.bean.BaseResultBean;

public class AemsThirdPartyBankCardPlusResultBean extends BaseResultBean {
	
	/**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3709370958884607483L;
    private String srvAuthCode;

    public String getSrvAuthCode() {
        return srvAuthCode;
    }

    public void setSrvAuthCode(String srvAuthCode) {
        this.srvAuthCode = srvAuthCode;
    }
	
	
}
