package com.hyjf.app.user.credit;

import java.util.List;

public class TenderProjectDetailBean {

	/**
	 *  列表字段id
	 *	string 
	 *	example: basicMsg
	 */
	private String id;
	
	/**
	 *  列表字段名
	 *	string 
	 *	example: 基础信息
	 */
	private String title;
	
	/**
	 *  列表字段
	 *	string 
	 *	example: list
	 */
	private List<TenderCreditBorrowBean> msg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<TenderCreditBorrowBean> getMsg() {
		return msg;
	}

	public void setMsg(List<TenderCreditBorrowBean> msg) {
		this.msg = msg;
	}

	
}
