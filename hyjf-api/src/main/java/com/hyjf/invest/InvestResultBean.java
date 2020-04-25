package com.hyjf.invest;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

public class InvestResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;
    /**预期收益*/
    private String interest;
    /**是否有优惠券*/
    private String isThereCoupon;
    /**优惠券id*/
    private String couponId;
    /**优惠券描述*/
    private String couponDescribe;
    /**类别*/
    private String couponType;
    /**类别*/
    private String couponTypeStr;
    /**标题*/
    private String couponName;
    /**额度*/
    private String couponQuota;
    /**有效期*/
    private String endTime;
    /**真实出借金额*/
    private String realAmount;
    
    /** 确认真实出借收益信息 */
    private String borrowInterest;
    /** 确认优惠券收益信息 */
    private String capitalInterest;
    /** 确认是否使用优惠券 */
    private String isUsedCoupon;
    
    /**可用优惠券数量*/
    private String couponAvailableCount;


    

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
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

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getCouponAvailableCount() {
        return couponAvailableCount;
    }

    public void setCouponAvailableCount(String couponAvailableCount) {
        this.couponAvailableCount = couponAvailableCount;
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

    public String getCouponTypeStr() {
        return couponTypeStr;
    }

    public void setCouponTypeStr(String couponTypeStr) {
        this.couponTypeStr = couponTypeStr;
    }

	
	
}
