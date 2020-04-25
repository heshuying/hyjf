/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.app.user.recharge;

import com.hyjf.app.BaseResultBean;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月22日
 * @see 上午8:50:38
 */
@Deprecated
public class RechargeUrlResultVo extends BaseResultBean {

    public RechargeUrlResultVo(String request) {
        super(request);
    }

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -4176414696527299618L;

    public String getRechargeUrl() {
        return rechargeUrl;
    }

    public void setRechargeUrl(String rechargeUrl) {
        this.rechargeUrl = rechargeUrl;
    }

    private String rechargeUrl = "";

}
