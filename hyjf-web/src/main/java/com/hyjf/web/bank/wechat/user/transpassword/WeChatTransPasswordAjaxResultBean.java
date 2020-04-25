/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.wechat.user.transpassword;

import java.io.Serializable;

import com.hyjf.bank.service.BaseAjaxResultBean;

/**
 * ajax数据返回基类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 下午2:45:31
 */
public class WeChatTransPasswordAjaxResultBean extends BaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3059306057064822540L;
	
	/**最大间隔时间*/
	private Integer maxValidTime;
	/**业务授权码*/
	private String srvAuthCode;
    public Integer getMaxValidTime() {
        return maxValidTime;
    }

    public void setMaxValidTime(Integer maxValidTime) {
        this.maxValidTime = maxValidTime;
    }

    public String getSrvAuthCode() {
        return srvAuthCode;
    }

    public void setSrvAuthCode(String srvAuthCode) {
        this.srvAuthCode = srvAuthCode;
    }

	

}
