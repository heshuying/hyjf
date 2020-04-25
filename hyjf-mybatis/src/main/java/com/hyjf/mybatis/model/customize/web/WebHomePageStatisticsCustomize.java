package com.hyjf.mybatis.model.customize.web;

public class WebHomePageStatisticsCustomize {
	
	//系统累计出借金额
	public String totalSum;
	//风险保证金
	public String bailTotal;
	//累计创造收益
	public String totalInterest; 

	public WebHomePageStatisticsCustomize() {
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

	public String getBailTotal() {
		return bailTotal;
	}

	public void setBailTotal(String bailTotal) {
		this.bailTotal = bailTotal;
	}

}
