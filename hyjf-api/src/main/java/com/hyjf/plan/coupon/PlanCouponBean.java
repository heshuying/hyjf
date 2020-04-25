package com.hyjf.plan.coupon;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.base.bean.BaseBean;

public class PlanCouponBean extends BaseBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162226L;
	//出借标号
	private String planNid;
	//平台标识
    private String platform="1";
	//真实出借金额
    private String money="0";
    //优惠券编号
    private String couponGrantId;
    //出借设备ip
    private String ip;
    //用户优惠券状态
    private String usedFlag;
    //真实出借订单号
    private String ordId=StringUtils.EMPTY;
    //排他check
    private int couponOldTime;
    /**
     * 还款时间
     */
    private Integer recoverTime;
    

    
    
    public Integer getRecoverTime() {
        return recoverTime;
    }

    public void setRecoverTime(Integer recoverTime) {
        this.recoverTime = recoverTime;
    }

    public String getPlanNid() {
        return planNid;
    }

    public void setPlanNid(String planNid) {
        this.planNid = planNid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCouponGrantId() {
        return couponGrantId;
    }

    public void setCouponGrantId(String couponGrantId) {
        this.couponGrantId = couponGrantId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getCouponOldTime() {
        return couponOldTime;
    }

    public void setCouponOldTime(int couponOldTime) {
        this.couponOldTime = couponOldTime;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getUsedFlag() {
        return usedFlag;
    }

    public void setUsedFlag(String usedFlag) {
        this.usedFlag = usedFlag;
    }
    
    
}
