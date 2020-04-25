package com.hyjf.mybatis.model.customize.act;

import java.math.BigDecimal;

public class ActNovPrizeDetailCustomize {
	private Integer prizeId;

	private String prizeName;

	private BigDecimal price;

	private BigDecimal priceGoal;
	
	private Integer amountTotal;
	
	private Integer remainCount;
	
	//当前价格
	private BigDecimal prizeNow;
	
	//是否可以购买 0否 1可以
	private String isCanBuy;

	// 0:用户尚未砍价 1：用户已砍价
	private String hasBargain;
	
	// 0不可以帮砍 1：可以帮砍
	private String isCanHelpBargain;

	public Integer getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceGoal() {
		return priceGoal;
	}

	public void setPriceGoal(BigDecimal priceGoal) {
		this.priceGoal = priceGoal;
	}

	public String getHasBargain() {
		return hasBargain;
	}

	public void setHasBargain(String hasBargain) {
		this.hasBargain = hasBargain;
	}

	public Integer getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(Integer amountTotal) {
		this.amountTotal = amountTotal;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	public BigDecimal getPrizeNow() {
		return prizeNow;
	}

	public void setPrizeNow(BigDecimal prizeNow) {
		this.prizeNow = prizeNow;
	}

	public String getIsCanBuy() {
		return isCanBuy;
	}

	public void setIsCanBuy(String isCanBuy) {
		this.isCanBuy = isCanBuy;
	}

	public String getIsCanHelpBargain() {
		return isCanHelpBargain;
	}

	public void setIsCanHelpBargain(String isCanHelpBargain) {
		this.isCanHelpBargain = isCanHelpBargain;
	}

	
}
