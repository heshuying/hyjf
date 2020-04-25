package com.hyjf.mybatis.model.customize.apiweb;

public class ApiwebTenderInfoCustomize {

	/**
	 * 订单号
	 */
	private String oid;
	
	/**
	 * 平台标的唯一编号
	 */
	private String bid;
	
	/**
	 * 平台标的名称
	 */
	private String title;
	
	/**
	 * 平台标的访问url
	 */
	private String url;
	
	/**
	 * 出借金额
	 */
	private String amount;
	
	/**
	 * 出借时间
	 */
	private String investtime;
	
	/**
	 * 借款期限
	 */
	private String period;
	
	/**
	 * 借款类型
	 */
	private String unit;
	
	/**
	 * 年华利率
	 */
	private String rate;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInvesttime() {
		return investtime;
	}

	public void setInvesttime(String investtime) {
		this.investtime = investtime;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
	
}
