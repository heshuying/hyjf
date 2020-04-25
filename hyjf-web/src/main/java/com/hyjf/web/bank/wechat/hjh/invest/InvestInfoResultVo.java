/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.bank.wechat.hjh.invest;

import com.hyjf.web.BaseBean;

/**
 * 获取出借信息VO对象
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月25日
 * @see 上午10:44:31
 */
public class InvestInfoResultVo extends BaseBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -2087974873373127422L;

    private String borrowNid;

    private String borrowAccountWait;

    private String balance;

    private String interest;

    private String prospectiveEarnings;

    /** 是否有优惠券 */
    private String isThereCoupon;

    /** 优惠券id */
    private String couponId;

    /** 优惠券描述 */
    private String couponDescribe;

    /** 类别 */
    private String couponType;

    /** 标题 */
    private String couponName;

    /** 额度 */
    private String couponQuota;

    /** 有效期 */
    private String endTime;

    private String realAmount;
    
    /** 确认真实出借信息 */
    private String confirmRealAmount;
    /** 确认优惠券信息 */
    private String confirmCouponDescribe;
    /** 确认真实出借收益信息 */
    private String borrowInterest;
    /** 确认优惠券收益信息 */
    private String capitalInterest;
    /** 确认是否使用优惠券 */
    private String isUsedCoupon;
    
    /** 可用优惠券数量 */
    private String couponAvailableCount;

    // 出借描述
    private String investmentDescription;

    // 起投金额
    private String initMoney;
    // 倍增金额
    private String increaseMoney;

    /** 全投金额 */
    private String investAllMoney;

    /** 认购本金 */
    private String assignCapital;

    /** 实际支付 */
    private String assignPay;

    /** 折价率 */
    private String creditDiscount;

    /** 垫付利息 */
    private String assignInterestAdvance;

    /** 实际支付计算式 */
    private String assignPayText;
    
    /** 居间服务协议url */
    private String protocolUrl1;
    
    /** 散标风险揭示书协议url */
    private String protocolUrl2;
    
    /** 温州金融资产交易中心个人会员服务协议url  */
    private String protocolUrl3;

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getBorrowAccountWait() {
        return borrowAccountWait;
    }

    public void setBorrowAccountWait(String borrowAccountWait) {
        this.borrowAccountWait = borrowAccountWait;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getProspectiveEarnings() {
        if ("0.00".equals(prospectiveEarnings)) {
            return "";
        }
        return prospectiveEarnings;
    }

    public void setProspectiveEarnings(String prospectiveEarnings) {
        this.prospectiveEarnings = prospectiveEarnings;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInvestmentDescription() {
        return investmentDescription;
    }

    public void setInvestmentDescription(String investmentDescription) {
        this.investmentDescription = investmentDescription;
    }

    public String getInitMoney() {
        return initMoney;
    }

    public void setInitMoney(String initMoney) {
        this.initMoney = initMoney;
    }

    public String getIsThereCoupon() {
        return isThereCoupon;
    }

    public void setIsThereCoupon(String isThereCoupon) {
        this.isThereCoupon = isThereCoupon;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponDescribe() {
        return couponDescribe;
    }

    public void setCouponDescribe(String couponDescribe) {
        this.couponDescribe = couponDescribe;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponQuota() {
        return couponQuota;
    }

    public void setCouponQuota(String couponQuota) {
        this.couponQuota = couponQuota;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCouponAvailableCount() {
        return couponAvailableCount;
    }

    public void setCouponAvailableCount(String couponAvailableCount) {
        this.couponAvailableCount = couponAvailableCount;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getInvestAllMoney() {
        return investAllMoney;
    }

    public void setInvestAllMoney(String investAllMoney) {
        this.investAllMoney = investAllMoney;
    }

    public String getAssignCapital() {
        return assignCapital;
    }

    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }

    public String getAssignPay() {
        return assignPay;
    }

    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }

    public String getCreditDiscount() {
        return creditDiscount;
    }

    public void setCreditDiscount(String creditDiscount) {
        this.creditDiscount = creditDiscount;
    }

    public String getAssignInterestAdvance() {
        return assignInterestAdvance;
    }

    public void setAssignInterestAdvance(String assignInterestAdvance) {
        this.assignInterestAdvance = assignInterestAdvance;
    }

    public String getAssignPayText() {
        return assignPayText;
    }

    public void setAssignPayText(String assignPayText) {
        this.assignPayText = assignPayText;
    }

	public String getProtocolUrl1() {
		return protocolUrl1;
	}

	public void setProtocolUrl1(String protocolUrl1) {
		this.protocolUrl1 = protocolUrl1;
	}

	public String getProtocolUrl2() {
		return protocolUrl2;
	}

	public void setProtocolUrl2(String protocolUrl2) {
		this.protocolUrl2 = protocolUrl2;
	}

    public String getConfirmRealAmount() {
        return confirmRealAmount;
    }

    public void setConfirmRealAmount(String confirmRealAmount) {
        this.confirmRealAmount = confirmRealAmount;
    }

    public String getConfirmCouponDescribe() {
        return confirmCouponDescribe;
    }

    public void setConfirmCouponDescribe(String confirmCouponDescribe) {
        this.confirmCouponDescribe = confirmCouponDescribe;
    }

    public String getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(String borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public String getCapitalInterest() {
        return capitalInterest;
    }

    public void setCapitalInterest(String capitalInterest) {
        this.capitalInterest = capitalInterest;
    }

    public String getIsUsedCoupon() {
        return isUsedCoupon;
    }

    public void setIsUsedCoupon(String isUsedCoupon) {
        this.isUsedCoupon = isUsedCoupon;
    }

	public String getProtocolUrl3() {
		return protocolUrl3;
	}

	public void setProtocolUrl3(String protocolUrl3) {
		this.protocolUrl3 = protocolUrl3;
	}

	public String getIncreaseMoney() {
		return increaseMoney;
	}

	public void setIncreaseMoney(String increaseMoney) {
		this.increaseMoney = increaseMoney;
	}
	
	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    private String status;

    private String statusDesc;
	
}
