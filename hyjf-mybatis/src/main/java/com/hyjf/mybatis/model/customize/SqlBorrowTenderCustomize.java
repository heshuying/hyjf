package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

public class SqlBorrowTenderCustomize implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//huiyingdai_borrow_tender
	private BigDecimal account; 
	private String addtime;
	private int user_id;
	
	//oa_department
	private String neme;
	private String firstdepartname;
	private String seconddepartname;
	
	//huiyingdai_users
	private String username;
	
	//oa_users
	private String user_realname;

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

//	public int getAddtime() {
//		return addtime;
//	}
//
//	public void setAddtime(int addtime) {
//		this.addtime = addtime;
//	}
	
	

	public int getUser_id() {
		return user_id;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getNeme() {
		return neme;
	}

	public void setNeme(String neme) {
		this.neme = neme;
	}

	public String getFirstdepartname() {
		return firstdepartname;
	}

	public void setFirstdepartname(String firstdepartname) {
		this.firstdepartname = firstdepartname;
	}

	public String getSeconddepartname() {
		return seconddepartname;
	}

	public void setSeconddepartname(String seconddepartname) {
		this.seconddepartname = seconddepartname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_realname() {
		return user_realname;
	}

	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
