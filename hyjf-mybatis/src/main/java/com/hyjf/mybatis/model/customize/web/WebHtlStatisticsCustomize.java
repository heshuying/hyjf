package com.hyjf.mybatis.model.customize.web;

public class WebHtlStatisticsCustomize {
	
	//汇天利累计交易额
	public String totalSum;
	//累计创造收益
	public String totalInterest; 

	public WebHtlStatisticsCustomize() {
		super();
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(String totalInterest) {
		this.totalInterest = totalInterest;
	}

}
