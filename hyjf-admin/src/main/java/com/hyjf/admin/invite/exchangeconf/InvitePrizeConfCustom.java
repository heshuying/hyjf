package com.hyjf.admin.invite.exchangeconf;

import com.hyjf.mybatis.model.auto.InvitePrizeConf;

public class InvitePrizeConfCustom extends InvitePrizeConf{
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    private String couponCodes;

    public String getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(String couponCodes) {
        this.couponCodes = couponCodes;
    }
    
    
}
