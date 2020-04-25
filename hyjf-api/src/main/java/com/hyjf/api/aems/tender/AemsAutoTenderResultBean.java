package com.hyjf.api.aems.tender;

import com.hyjf.base.bean.BaseResultBean;


/**
 * 自动投资返回参数
 * @author xiaojohn
 *
 */
public class AemsAutoTenderResultBean extends BaseResultBean {

	private static final long serialVersionUID = 1L;

	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

    
}
