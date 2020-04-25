package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;
/**
  * @ClassName: HtlUserInfo
  * @Description: 汇天利用户信息
  * @author Michael
  * @date 2015年11月25日 上午10:42:44
 */
public class HtlUserInfoCustomize implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int userid;//用户id
	private String username;//用户名
	private int referid;//推荐人id
	private String refername;//推荐人
	private BigDecimal currentEarnings;//当前收益
	private BigDecimal principal;//本金
	private BigDecimal todayEarnings;//今日预计收益
	private BigDecimal weekEarnings;//近一周收益
	private BigDecimal monthEarnings;//近一月收益
	private BigDecimal historyEarnings;//历史累计收益
	private String truename;//真实姓名
	private String mobile;//手机号
	protected int limitStart = -1;

    protected int limitEnd = -1;
	
    public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getLimitStart() {
		return limitStart;
	}
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}
	public int getLimitEnd() {
		return limitEnd;
	}
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getReferid() {
		return referid;
	}
	public void setReferid(int referid) {
		this.referid = referid;
	}
	public String getRefername() {
		return refername;
	}
	public void setRefername(String refername) {
		this.refername = refername;
	}
	public BigDecimal getCurrentEarnings() {
		return currentEarnings;
	}
	public void setCurrentEarnings(BigDecimal currentEarnings) {
		this.currentEarnings = currentEarnings;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getTodayEarnings() {
		return todayEarnings;
	}
	public void setTodayEarnings(BigDecimal todayEarnings) {
		this.todayEarnings = todayEarnings;
	}
	public BigDecimal getWeekEarnings() {
		return weekEarnings;
	}
	public void setWeekEarnings(BigDecimal weekEarnings) {
		this.weekEarnings = weekEarnings;
	}
	public BigDecimal getMonthEarnings() {
		return monthEarnings;
	}
	public void setMonthEarnings(BigDecimal monthEarnings) {
		this.monthEarnings = monthEarnings;
	}
	public BigDecimal getHistoryEarnings() {
		return historyEarnings;
	}
	public void setHistoryEarnings(BigDecimal historyEarnings) {
		this.historyEarnings = historyEarnings;
	}
}
