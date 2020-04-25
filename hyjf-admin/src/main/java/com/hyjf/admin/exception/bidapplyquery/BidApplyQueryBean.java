/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
/**
 * 
 */
package com.hyjf.admin.exception.bidapplyquery;

import java.io.Serializable;
import com.alibaba.fastjson.JSONObject;
/**
 * @author libin
 * 出借人投标申请查询Bean
 * @version BidApplyQueryBean.java, v0.1 2018年8月16日 上午9:50:59
 */
public class BidApplyQueryBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 检索条件:用户ID
	 */
	private Integer userId;
	
	/**
	 * 检索条件:电子账号 长度19 出借人电子账号
	 */
	private String accountId;
	
	/**
	 * 检索条件:原订单号  长度30 原投标订单号
	 */
	private String orgOrderId;
	
	/**
	 * 结果
	 */
	private JSONObject result;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOrgOrderId() {
		return orgOrderId;
	}

	public void setOrgOrderId(String orgOrderId) {
		this.orgOrderId = orgOrderId;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}
}
