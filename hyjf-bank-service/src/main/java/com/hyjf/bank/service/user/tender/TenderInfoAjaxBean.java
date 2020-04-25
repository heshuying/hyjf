package com.hyjf.bank.service.user.tender;

import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;

public class TenderInfoAjaxBean extends BaseAjaxResultBean {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -8830215900809194471L;
	/**收益*/
	private String earnings = "0";
	/**是否有最优优惠券*/
	private Integer isThereCoupon;
	/**最优优惠券信息*/
	private UserCouponConfigCustomize couponConfig;
	/**可用优惠券数量*/
	private Integer couponAvailableCount;
	/**是否是vip*/
	private Integer ifVip;
	/**用户优惠券数量*/
	private Integer recordTotal;
	/**真实出借预期收益*/
	private String capitalInterest;
	/**真实优惠券出借预期收益*/
    private String couponCapitalInterest;
    /**产品加息利息*/
    private String borrowExtraYield;
    
	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

    public Integer getIsThereCoupon() {
        return isThereCoupon;
    }

    public void setIsThereCoupon(Integer isThereCoupon) {
        this.isThereCoupon = isThereCoupon;
    }

    public UserCouponConfigCustomize getCouponConfig() {
        return couponConfig;
    }

    public void setCouponConfig(UserCouponConfigCustomize couponConfig) {
        this.couponConfig = couponConfig;
    }

    public Integer getCouponAvailableCount() {
        return couponAvailableCount;
    }

    public void setCouponAvailableCount(Integer couponAvailableCount) {
        this.couponAvailableCount = couponAvailableCount;
    }

    public Integer getIfVip() {
        return ifVip;
    }

    public void setIfVip(Integer ifVip) {
        this.ifVip = ifVip;
    }

    public Integer getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(Integer recordTotal) {
        this.recordTotal = recordTotal;
    }

    public String getCapitalInterest() {
        return capitalInterest;
    }

    public void setCapitalInterest(String capitalInterest) {
        this.capitalInterest = capitalInterest;
    }

    public String getCouponCapitalInterest() {
        return couponCapitalInterest;
    }

    public void setCouponCapitalInterest(String couponCapitalInterest) {
        this.couponCapitalInterest = couponCapitalInterest;
    }

    public String getBorrowExtraYield() {
        return borrowExtraYield;
    }

    public void setBorrowExtraYield(String borrowExtraYield) {
        this.borrowExtraYield = borrowExtraYield;
    }

}
