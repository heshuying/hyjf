package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

public class SqlBorrowRecoverCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//huiyingdai_borrow_recover
	private BigDecimal recover_account;
	private BigDecimal recover_account_yes;
	private BigDecimal sum;
	private int recover_time;
	
	// huiyingdai_spreads_users
	private int spreads_userid;

	// oa_department
	private String neme;
	private String firstdepartname;
	private String seconddepartname;
	private String fgs;
	private String daqu;
	
	
	public int getRecover_time() {
		return recover_time;
	}
	public void setRecover_time(int recover_time) {
		this.recover_time = recover_time;
	}
	public BigDecimal getRecover_account() {
		return recover_account;
	}
	public void setRecover_account(BigDecimal recover_account) {
		this.recover_account = recover_account;
	}
	public BigDecimal getRecover_account_yes() {
		return recover_account_yes;
	}
	public void setRecover_account_yes(BigDecimal recover_account_yes) {
		this.recover_account_yes = recover_account_yes;
	}
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	public int getSpreads_userid() {
		return spreads_userid;
	}
	public void setSpreads_userid(int spreads_userid) {
		this.spreads_userid = spreads_userid;
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
	public String getFgs() {
		return fgs;
	}
	public void setFgs(String fgs) {
		this.fgs = fgs;
	}
	public String getDaqu() {
		return daqu;
	}
	public void setDaqu(String daqu) {
		this.daqu = daqu;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
