/**
 * 首页实体
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.web.bank.wechat.hjh.coupon;

import java.io.Serializable;

public class CouponBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;
	//用户优惠券id
    private String userCouponId;
	//优惠券类型
	private String couponType;
	//优惠券类型
    private Integer couponTypeStr;
	//优惠券名称
	private String couponName;
	//优惠券额度
    private String couponQuota;
    //优惠券有效期
    private String endTime;
    //出借金额
    private String tenderQuota;
    //备注
    private String remarks;
    
    public String getUserCouponId() {
        return userCouponId;
    }
    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
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
    public String getTenderQuota() {
        return tenderQuota;
    }
    public void setTenderQuota(String tenderQuota) {
        this.tenderQuota = tenderQuota;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getCouponTypeStr() {
        return couponTypeStr;
    }
    public void setCouponTypeStr(Integer couponTypeStr) {
        this.couponTypeStr = couponTypeStr;
    }
    
    
}
