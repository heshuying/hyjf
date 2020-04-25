package com.hyjf.coupon.myaccount;

import com.hyjf.base.bean.BaseResultBean;

public class VipInfoResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 7981718231881189938L;
    
    private String isVip; // 用户是否是vip(0不是vip，1是vip)
    
    private String vipPictureUrl; // vip名称图片路径
    
    private String vipLevel; // vip等级图片路径
    
    private String vipJumpUrl; // vip按钮跳转路径
    
    private String readFlag; // 优惠券更新标识

    private String couponDescription = "福利多多哒"; // 优惠券描述

    private String isUpdate; // 我的账户更新标识(0未更新，1已更新)

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getVipPictureUrl() {
        return vipPictureUrl;
    }

    public void setVipPictureUrl(String vipPictureUrl) {
        this.vipPictureUrl = vipPictureUrl;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getVipJumpUrl() {
        return vipJumpUrl;
    }

    public void setVipJumpUrl(String vipJumpUrl) {
        this.vipJumpUrl = vipJumpUrl;
    }
    

}
