/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.wechat.user.tender;

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
public class TenderAjaxResultBean extends WeChatBaseAjaxResultBean implements Serializable {

	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3167525933975111769L;
	
	/**标的待出借金额*/
	private String wait;
	
	/**历史回报*/
	private String earnings;
	
	/**出借url*/
	private String url;

	public String getWait() {
		return wait;
	}

	public void setWait(String wait) {
		this.wait = wait;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
