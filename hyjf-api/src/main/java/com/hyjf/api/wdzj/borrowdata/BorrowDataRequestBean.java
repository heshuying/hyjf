package com.hyjf.api.wdzj.borrowdata;

import com.hyjf.base.bean.BaseBean;

public class BorrowDataRequestBean extends BaseBean{

	private String token;
	private String date;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
    
}
