package com.hyjf.wrb.regiester;

import com.hyjf.wrb.WrbResponse;

public class WrbRegisterResultBean extends WrbResponse{

	/**
	 * 汇盈金服用户ID
	 */
	private String pf_user_id;
	/**
	 * 汇盈金服用户名
	 */
	private String pf_user_name;

	public String getPf_user_id() {
		return pf_user_id;
	}

	public void setPf_user_id(String pf_user_id) {
		this.pf_user_id = pf_user_id;
	}

	public String getPf_user_name() {
		return pf_user_name;
	}

	public void setPf_user_name(String pf_user_name) {
		this.pf_user_name = pf_user_name;
	}
	
}
