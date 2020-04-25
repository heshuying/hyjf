/**
 * 首页实体
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.web.coupon;

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
    
    //主键id
    private String couponUserCode;
    //备注
    private String remarks;
    
    //优惠券开始时间
    private String couponAddTime;
    //优惠券开始时间
    private String couponEndTime;
    
    
    /************************网站改版新加字段 pcc start******************************/
    //使用项目类别
    private String projectType;
    //项目期限
    private String projectExpiration;
    //优惠券的使用平台
    private String couponSystem;
    /************************网站改版新加字段 pcc end******************************/
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
    public String getCouponUserCode() {
        return couponUserCode;
    }
    public void setCouponUserCode(String couponUserCode) {
        this.couponUserCode = couponUserCode;
    }
    public String getCouponAddTime() {
        return couponAddTime;
    }
    public void setCouponAddTime(String couponAddTime) {
        this.couponAddTime = couponAddTime;
    }
    public String getCouponEndTime() {
        return couponEndTime;
    }
    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }
    public Integer getCouponTypeStr() {
        return couponTypeStr;
    }
    public void setCouponTypeStr(Integer couponTypeStr) {
        this.couponTypeStr = couponTypeStr;
    }
    public String getProjectType() {
        return projectType;
    }
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    public String getProjectExpiration() {
        return projectExpiration;
    }
    public void setProjectExpiration(String projectExpiration) {
        this.projectExpiration = projectExpiration;
    }
    public String getCouponSystem() {
        return couponSystem;
    }
    public void setCouponSystem(String couponSystem) {
        this.couponSystem = couponSystem;
    }

    
}
