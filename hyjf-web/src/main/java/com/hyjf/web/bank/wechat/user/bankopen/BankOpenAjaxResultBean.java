/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.wechat.user.bankopen;

import java.io.Serializable;

import com.hyjf.web.WeChatBaseAjaxResultBean;

/**
 * ajax数据返回基类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 下午2:45:31
 */
public class BankOpenAjaxResultBean extends WeChatBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3059306057064822540L;
	
	/**开户订单号*/
	private String logOrderId;

	public String getLogOrderId() {
		return logOrderId;
	}

	public void setLogOrderId(String logOrderId) {
		this.logOrderId = logOrderId;
	}

}
