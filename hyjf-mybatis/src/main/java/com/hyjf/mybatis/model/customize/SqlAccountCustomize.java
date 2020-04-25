package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

public class SqlAccountCustomize implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//huiyingdai_account
	private BigDecimal balance;
	private BigDecimal sum;
	
	//huiyingdai_spreads_users
	private int spreads_userid;
	
	//oa_department
	private String neme;
	private String firstdepartname;
	private String seconddepartname;
	private String fgs;
	private String daqu;
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDaqu() {
		return daqu;
	}
	public void setDaqu(String daqu) {
		this.daqu = daqu;
	}
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	
	
	
	

}
