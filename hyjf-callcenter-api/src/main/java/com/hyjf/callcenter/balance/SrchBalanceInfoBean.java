package com.hyjf.callcenter.balance;

/**
 * Description:江西银行账户实体类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */
import java.io.Serializable;
import java.math.BigDecimal;

public class SrchBalanceInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2772904333530569316L;
	/**
	 * 请求时间戳(暂时)
	 */
	private String timestamp;
	/**
	 * md5校验码(暂时)
	 */
	private String sign;
	//以下为需求定义字段
	/**
	 * 序号（自增的，暂时）
	 */
	private String creditId;
	/**
	 * 1.用户名
	 */
	private String username;
	/**
	 * 2.手机号
	 */
	private String mobile;
	/**
	 * 3.资产总额 (admin资金总额)
	 */
	private BigDecimal bankTotal;
	/**
	 * 4.待收金额 (admin银行待收)
	 */
	private BigDecimal bankAwait;
	/**
	 * 5.待还总额(admin待还金额银行待还)
	 */
	private BigDecimal bankWaitRepay;
	
	//6.江西银行总可用金额  (admin总可用金额)
	/**
	 * 8.银行可用金额
	 */
	private BigDecimal bankBalance;
	/**
	 * 江西银行可提现金额
	 */
	private BigDecimal bankBalanceCash;
	
	//7.江西银行总冻结金额 (admin总冻结金额)
	/**
	 * 9.银行冻结金额
	 */
	private BigDecimal bankFrost;
	/**
	 * 江西银行冻结金额
	 */
	private BigDecimal bankFrostCash;
	
	/**
	 * 10.汇付可用金额 (admin总可用金额)
	 */
	private BigDecimal balanceTotal;
	/**
	 * 11.汇付冻结金额(admin总冻结金额)
	 */
	private BigDecimal frostTotal;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCreditId() {
		return creditId;
	}
	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getBankTotal() {
		return bankTotal;
	}
	public void setBankTotal(BigDecimal bankTotal) {
		this.bankTotal = bankTotal;
	}
	public BigDecimal getBankAwait() {
		return bankAwait;
	}
	public void setBankAwait(BigDecimal bankAwait) {
		this.bankAwait = bankAwait;
	}
	public BigDecimal getBankWaitRepay() {
		return bankWaitRepay;
	}
	public void setBankWaitRepay(BigDecimal bankWaitRepay) {
		this.bankWaitRepay = bankWaitRepay;
	}
	public BigDecimal getBankBalance() {
		return bankBalance;
	}
	public void setBankBalance(BigDecimal bankBalance) {
		this.bankBalance = bankBalance;
	}
	public BigDecimal getBankBalanceCash() {
		return bankBalanceCash;
	}
	public void setBankBalanceCash(BigDecimal bankBalanceCash) {
		this.bankBalanceCash = bankBalanceCash;
	}
	public BigDecimal getBankFrost() {
		return bankFrost;
	}
	public void setBankFrost(BigDecimal bankFrost) {
		this.bankFrost = bankFrost;
	}
	public BigDecimal getBankFrostCash() {
		return bankFrostCash;
	}
	public void setBankFrostCash(BigDecimal bankFrostCash) {
		this.bankFrostCash = bankFrostCash;
	}
	public BigDecimal getBalanceTotal() {
		return balanceTotal;
	}
	public void setBalanceTotal(BigDecimal balanceTotal) {
		this.balanceTotal = balanceTotal;
	}
	public BigDecimal getFrostTotal() {
		return frostTotal;
	}
	public void setFrostTotal(BigDecimal frostTotal) {
		this.frostTotal = frostTotal;
	}

}
