package com.hyjf.mybatis.model.customize.act;

import java.math.BigDecimal;

public class ActNovPrizeCustomize {
	private Integer prizeId;

	private String prizeName;

	private BigDecimal price;

	private BigDecimal priceGoal;
	
	private Integer remainCount;

	// 0:用户尚未砍价 1：用户已砍价
	private String hasBargain;

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

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	@Override
	public String toString() {
		return "ActNovPrizeCustomize [prizeId=" + prizeId + ", prizeName=" + prizeName + ", price=" + price
				+ ", priceGoal=" + priceGoal + ", remainCount=" + remainCount + ", hasBargain=" + hasBargain + "]";
	}

	
}
