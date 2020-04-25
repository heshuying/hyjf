
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author pcc
 */

public class VipAuthCustomize implements Serializable {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1392331406423663983L;
    /**vip礼包id*/
    private Integer id;
    /**vip等级名称*/
    private String vipName;
    /**vip等级名称*/
    private String vipId;
    /**vip等级*/
    private Integer vipLevel;
    /**优惠券编号*/
    private String couponCode;
    /**优惠券名称*/
    private String couponName;
    /**优惠券类别*/
    private Integer couponType;
    /**优惠券面值*/
    private String couponQuota;
    /**优惠券数量*/
    private Integer couponQuantity;
    
    
    /**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;

    
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;

    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getVipName() {
        return vipName;
    }
    public void setVipName(String vipName) {
        this.vipName = vipName;
    }
    public Integer getVipLevel() {
        return vipLevel;
    }
    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }
    public String getCouponCode() {
        return couponCode;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    public String getCouponName() {
        return couponName;
    }
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    public Integer getCouponType() {
        return couponType;
    }
    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }
    public String getCouponQuota() {
        return couponQuota;
    }
    public void setCouponQuota(String couponQuota) {
        this.couponQuota = couponQuota;
    }
    public Integer getCouponQuantity() {
        return couponQuantity;
    }
    public void setCouponQuantity(Integer couponQuantity) {
        this.couponQuantity = couponQuantity;
    }
    public String getTimeStartSrch() {
        return timeStartSrch;
    }
    public void setTimeStartSrch(String timeStartSrch) {
        this.timeStartSrch = timeStartSrch;
    }
    public String getTimeEndSrch() {
        return timeEndSrch;
    }
    public void setTimeEndSrch(String timeEndSrch) {
        this.timeEndSrch = timeEndSrch;
    }
    public int getLimitStart() {
        return limitStart;
    }
    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }
    public int getLimitEnd() {
        return limitEnd;
    }
    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }
    public String getVipId() {
        return vipId;
    }
    public void setVipId(String vipId) {
        this.vipId = vipId;
    }
    
    
}

    