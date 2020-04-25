/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.api.surong.user.bankopen;

import java.io.Serializable;

import com.hyjf.base.bean.BaseBean;


/**
 * ajax数据返回基类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 下午2:45:31
 */
public class OpenAjaxResultBean extends BaseBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3059306057064822540L;
	
	/**开户订单号*/
	private String logOrderId;

	// 请求处理是否成功  0成功 1失败
    private int status = 0;
    // 返回信息
    private String returnMsg;
    
    
    
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getLogOrderId() {
		return logOrderId;
	}

	public void setLogOrderId(String logOrderId) {
		this.logOrderId = logOrderId;
	}

}
