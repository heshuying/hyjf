package com.hyjf.mybatis.model.customize.wrb;

import java.math.BigDecimal;

/**
 * @author xiasq
 * @version WrbBorrowTenderCustomize, v0.1 2018/3/9 11:47
 * 标的出借汇总数据
 */
public class WrbBorrowTenderSumCustomize {
	
	// 借款人id
	private String borrowId;
  
	// 首笔出借时间
    private String firstInvestTime;
    // 末笔出借时间
    private String lastInvestTime;
    // 标的出借人数
    private String allInvestors;
    // 标的已投金额
    private BigDecimal investAllMoney;

    public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getFirstInvestTime() {
        return firstInvestTime;
    }

    public void setFirstInvestTime(String firstInvestTime) {
        this.firstInvestTime = firstInvestTime;
    }

    public String getLastInvestTime() {
        return lastInvestTime;
    }

    public void setLastInvestTime(String lastInvestTime) {
        this.lastInvestTime = lastInvestTime;
    }

    public String getAllInvestors() {
        return allInvestors;
    }

    public void setAllInvestors(String allInvestors) {
        this.allInvestors = allInvestors;
    }

    public BigDecimal getInvestAllMoney() {
        return investAllMoney;
    }

    public void setInvestAllMoney(BigDecimal investAllMoney) {
        this.investAllMoney = investAllMoney;
    }
}
