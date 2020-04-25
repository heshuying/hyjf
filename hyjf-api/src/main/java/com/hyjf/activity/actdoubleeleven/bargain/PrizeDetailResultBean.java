package com.hyjf.activity.actdoubleeleven.bargain;

import java.math.BigDecimal;

import com.hyjf.base.bean.BaseResultBean;

public class PrizeDetailResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    private String actStartTime;
    
    private String actEndtime;
    
    private String nowTime;
    
	private Integer prizeId;

	private String prizeName;

	private BigDecimal price;

	private BigDecimal priceGoal;
	
	private BigDecimal prizeNow;

	// 0:用户尚未砍价 1：用户已砍价
	private String hasBargain;
	
	private Integer amountTotal;
	
	private Integer remainCount;
	
	//是否可以购买 0否 1可以
	private String isCanBuy;
    
	public String getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(String actStartTime) {
		this.actStartTime = actStartTime;
	}

	public String getActEndtime() {
		return actEndtime;
	}

	public void setActEndtime(String actEndtime) {
		this.actEndtime = actEndtime;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

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

	public BigDecimal getPrizeNow() {
		return prizeNow;
	}

	public void setPrizeNow(BigDecimal prizeNow) {
		this.prizeNow = prizeNow;
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

	public String getIsCanBuy() {
		return isCanBuy;
	}

	public void setIsCanBuy(String isCanBuy) {
		this.isCanBuy = isCanBuy;
	}

	@Override
	public String toString() {
		return super.toString() + "PrizeDetailResultBean [actStartTime=" + actStartTime + ", actEndtime=" + actEndtime + ", nowTime="
				+ nowTime + ", prizeId=" + prizeId + ", prizeName=" + prizeName + ", price=" + price + ", priceGoal="
				+ priceGoal + ", prizeNow=" + prizeNow + ", hasBargain=" + hasBargain + ", amountTotal=" + amountTotal
				+ ", remainCount=" + remainCount + ", isCanBuy=" + isCanBuy + "]";
	}

    
    
}
